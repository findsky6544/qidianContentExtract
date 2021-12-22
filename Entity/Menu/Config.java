package Entity.Menu;

public class Config {
	public boolean isAddVolumeName;
	public String addVolumeNameWhere;
	
	public Config() {
		this.isAddVolumeName = false;
		this.addVolumeNameWhere = "";
	}
	
	public Config(boolean isAddVolumeName,String addVolumeNameWhere) {
		this.isAddVolumeName = isAddVolumeName;
		this.addVolumeNameWhere = addVolumeNameWhere;
	}
}
