package Model;

public class Pow {
	private int pow;
	private int extraPow;
	
	public Pow(int pow, int extraPow) {
		this.pow = pow;
		this.extraPow = extraPow;
	}

	public int getPow() {
		return pow;
	}
	
	public void setPow(int pow) {
		this.pow = pow;
	}
	
	public int getExtraPow() {
		return extraPow;
	}
	
	public void increasePow(int extraPow) {
		if (pow < 200) {
			this.pow += extraPow;			
		}
	}
}
