import java.util.Random;
import java.util.Arrays;

public class Substitute implements SymCipher {
	
	private byte[] key;
	private byte[] rKey;

	public Substitute() {
		Random r = new Random();
		key = new byte[256];

		for (int i = 0; i < key.length; i++) {
			key[i] = (byte)(i - 128);
		}

		int rPos;
		byte temp;
		int maxCount = (int) Math.sqrt(key.length);
		for (int count = 0; count < maxCount; count++) {
			for (int i = 0; i < key.length; i++) {
				rPos = r.nextInt(key.length);
				temp = key[i];
				key[i] = key[rPos];
				key[rPos] = temp;
			}
		}

		rKey = new byte[key.length];
		for (int i = 0; i < key.length; i++) {
			rKey[key[i] + 128] = (byte)i;
		}
	}

	public Substitute(byte[] k) {
		if (k.length != 256)
			throw new IllegalArgumentException();
		key = k;
		rKey = new byte[key.length];

		for (int i = 0; i < key.length; i++) 
			rKey[key[i] + 128] = (byte)i;
	}

	public byte[] getKey() { return key; }

	public byte[] encode(String s) {
		byte[] in = s.getBytes();
		byte[] result = new byte[in.length];

		for (int i = 0; i < in.length; i++)
			result[i] = key[in[i]];

		return result;
	}

	public String decode(byte[] b) {
		byte[] result = new byte[b.length];

		for (int i = 0; i < b.length; i++) 
			result[i] = rKey[b[i] + 128];

		return new String(result);
	}
}