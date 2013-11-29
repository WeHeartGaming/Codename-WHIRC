/**
 * 
 */
package no.whg.whirc.models;

/**
 * @author Fredrik
 *
 */
public class Message {
	private String name;
	private String message;
	private String time;
	private int hashcode;
	
	public Message(String name, String message, String time, int hashcode){
		this.name = name;
		this.message = message;
		this.time = time;
		this.hashcode = hashcode;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}
	/**
	 * @param time the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}
	/**
	 * 
	 * @return
	 */
	public int getHashcode(){
		return hashcode;
	}

}
