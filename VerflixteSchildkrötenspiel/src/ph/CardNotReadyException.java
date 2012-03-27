package ph;

public class CardNotReadyException extends RuntimeException {

	public CardNotReadyException(String reason) {
		super(reason);
	}
}
