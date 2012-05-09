package raw.java.map;

import com.ericsson.otp.erlang.OtpErlangPid;

public class MapNode {
	/**
	 * int representing an empty square value: 0
	 */
	public static final int NONE = 0;
	/**
	 * int representing rabbit value: 1
	 */
	public static final int RABBIT = 1;
	/**
	 * int representing an wolf value: 2
	 */
	public static final int WOLF = 2;
	private int grassLevel;
	public int getGrassLevel() {
		return grassLevel;
	}

	public void setGrassLevel(int grassLevel) {
		this.grassLevel = grassLevel;
	}

	public OtpErlangPid getPid() {
		return pid;
	}

	public void setPid(OtpErlangPid pid) {
		this.pid = pid;
	}

	private int type;
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	private OtpErlangPid pid;
	/**
	 * Constructor for the MapNode.
	 * @param grassLevel the level of grass for this square
	 * @param type the type that populates this square
	 * @param pid the pid of the entity populating this square (null if it is empty)
	 */
	public MapNode(int grassLevel, int type, OtpErlangPid pid) {
		this.grassLevel = grassLevel;
		this.type = type;
		this.pid = pid;
	}
}
