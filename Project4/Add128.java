import java.util.Random;

public class Add128 implements SymCipher {
	
	private byte[] key;

	public Add128() {
		Random r = new Random();
		key = new byte[128];
		r.nextBytes(key);	
	}

	public Add128(byte[] k) {
		key = k;
	}

	public byte[] getKey() { return key; }

	public byte[] encode(String s) {
		byte[] in = s.getBytes();
		byte[] result = new byte[in.length];

		for (int i = 0; i < in.length; i++) 
			result[i] = (byte)(((in[i] + key[i % key.length]) + 256) % 128); // java mods negatives wrong

		return result;
	}

	public String decode(byte[] b) {
		byte[] result = new byte[b.length];

		for (int i = 0; i < b.length; i++) 
			result[i] = (byte)(((b[i] - key[i % key.length]) + 256) % 128); // java mods negatives wrong

		return new String(result);
	}
}