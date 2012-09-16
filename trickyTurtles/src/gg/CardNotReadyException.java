package gg;

@SuppressWarnings("serial")
public class CardNotReadyException extends RuntimeException {

	public CardNotReadyException(String reason) {
		super(reason);
	}

	public CardNotReadyException() {
		super();
	}
}
