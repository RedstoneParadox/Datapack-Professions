package io.github.redstoneparadox.datapackprofessions.command;

import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.JsonOps;
import io.github.redstoneparadox.datapackprofessions.command.argument.TradeTableArgument;
import io.github.redstoneparadox.datapackprofessions.trades.TradeTable;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.quiltmc.loader.api.QuiltLoader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DumpTradesCommand implements Command<ServerCommandSource> {
	@Override
	public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var source = context.getSource();
		var argument = context.getArgument("table", TradeTableArgument.class);
		var table = argument.table();
		var identifier = argument.identifier();
		var result = TradeTable.CODEC.encodeStart(JsonOps.INSTANCE, table).result();
		File rootDirectory = new File(QuiltLoader.getGameDir().toFile(), "dumped-trades");
		File directory = new File(rootDirectory, identifier.getNamespace());
		File file = new File(directory, identifier.getPath() + ".json");

		result.ifPresent(json -> {
			try {
				var exists = true;

				if (!directory.exists()) {
					directory.mkdir();
				}

				if (!file.exists()) {
					exists = file.createNewFile();
				}

				if (exists) {
					var jsonWriter = new JsonWriter(new BufferedWriter(new FileWriter(file)));
					jsonWriter.setLenient(true);
					jsonWriter.setIndent("  ");
					Streams.write(json, jsonWriter);
					jsonWriter.close();
				}

			} catch (IOException e) {
				source.sendFeedback(() -> Text.translatable("commands.trades.dump.fail.exception", identifier, e.getClass().getName()), true);
				e.printStackTrace();
			}
		});

		source.sendFeedback(() -> Text.translatable("commands.trades.dump.success", identifier), true);

		return 0;
	}
}
