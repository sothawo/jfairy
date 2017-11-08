package io.codearte.jfairy.producer.person.locale.ge;

import io.codearte.jfairy.producer.BaseProducer;
import io.codearte.jfairy.producer.person.NationalIdentityCardNumberProvider;

import javax.inject.Inject;
import java.util.function.Supplier;

public class GeNationalIdentityCardNumberProvider implements NationalIdentityCardNumberProvider {

	private static class OldCardNumberProvider implements NationalIdentityCardNumberProvider {
		private static char[] GEORGIAN_CHAR = "აბგდევზთიკლმნოპჟრსტუფქღყშჩცძწჭხჯჰ".toCharArray();

		private final BaseProducer baseProducer;

		public OldCardNumberProvider(BaseProducer baseProducer) {
			this.baseProducer = baseProducer;
		}

		@Override
		public String get() {
			char geChar = GEORGIAN_CHAR[baseProducer.randomInt(GEORGIAN_CHAR.length - 1 )];
			return "N" + geChar + baseProducer.numerify("#######");
		}
	}

	private static class NewCardNumberProvider implements NationalIdentityCardNumberProvider {
		private final static String NEW_CARD_MASK = "##??#####";

		private final BaseProducer baseProducer;

		public NewCardNumberProvider(BaseProducer baseProducer) {
			this.baseProducer = baseProducer;
		}

		@Override
		public String get() {
			return baseProducer.bothify(NEW_CARD_MASK).toUpperCase();
		}
	}

	private final Supplier<NationalIdentityCardNumberProvider> formatPicker;

	@Inject
	public GeNationalIdentityCardNumberProvider(BaseProducer baseProducer) {
		NationalIdentityCardNumberProvider oldCardNumberProvider = new OldCardNumberProvider(baseProducer);
		NationalIdentityCardNumberProvider newCardNumberProvider = new NewCardNumberProvider(baseProducer);
		formatPicker = () -> baseProducer.randomElement(oldCardNumberProvider, newCardNumberProvider);
	}

	@Override
	public String get() {
		NationalIdentityCardNumberProvider numberProvider = formatPicker.get();
		return numberProvider.get();
	}
}
