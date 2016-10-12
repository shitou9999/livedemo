package tv.kuainiu.widget.tagview;

import java.io.Serializable;

public class Tag implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2684657309332033242L;
	
	private int backgroundResId=0;
	private int id=0;
	private boolean isChecked;
	private int leftDrawableResId;
	private int rightDrawableResId;
	private String name;

	public Tag() {
		
	}

	public Tag(int paramInt, String paramString) {
		this.id = paramInt;
		this.name = paramString;
	}

	public int getBackgroundResId() {
		return this.backgroundResId;
	}

	public int getId() {
		return this.id;
	}

	public int getLeftDrawableResId() {
		return this.leftDrawableResId;
	}

	public int getRightDrawableResId() {
		return this.rightDrawableResId;
	}

	public String getName() {
		return this.name;
	}

	public boolean isChecked() {
		return this.isChecked;
	}

	public void setBackgroundResId(int paramInt) {
		this.backgroundResId = paramInt;
	}

	public void setChecked(boolean paramBoolean) {
		this.isChecked = paramBoolean;
	}

	public void setId(int paramInt) {
		this.id = paramInt;
	}

	public void setLeftDrawableResId(int paramInt) {
		this.leftDrawableResId = paramInt;
	}

	public void setRightDrawableResId(int paramInt) {
		this.rightDrawableResId = paramInt;
	}

	public void setName(String paramString) {
		this.name = paramString;
	}
}
