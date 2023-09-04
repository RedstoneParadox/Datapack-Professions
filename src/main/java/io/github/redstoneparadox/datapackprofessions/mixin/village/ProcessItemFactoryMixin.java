package io.github.redstoneparadox.datapackprofessions.mixin.village;

import io.github.redstoneparadox.datapackprofessions.tradeoffer.ExtendedTradeOfferFactory;
import io.github.redstoneparadox.datapackprofessions.tradeoffer.TradeOfferFactoryType;
import net.minecraft.village.TradeOffers;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TradeOffers.ProcessItemFactory.class)
public abstract class ProcessItemFactoryMixin implements ExtendedTradeOfferFactory {
	@Override
	public TradeOfferFactoryType getType() {
		return TradeOfferFactoryType.VanillaTypes.PROCESS_ITEM_FACTORY_TYPE;
	}
}
