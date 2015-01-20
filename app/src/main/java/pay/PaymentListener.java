package pay;

public interface PaymentListener {
	public void onResult(String status, int requestFee, int payFee);
}
