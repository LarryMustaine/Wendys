package tools;

public class SoundObject{

	private String Category;
	private String Section;
	private String Name;
	private String Downloaded;
	private String URL;
	private String LocalPath;
	private String URLSound;
	private String DownloadedSound;
	private String LocalPathSound;

	public SoundObject() {
	}

	public void setCategory(String Category){
		this.Category = Category;
	}
	
	public void setSection(String Section){
		this.Section = Section;
	}
	
	public void setName(String Name){
		this.Name = Name;
	}
	
	public void setDownloaded(String Downloaded){
		this.Downloaded = Downloaded;
	}
	
	public void setURL(String URL){
		this.URL = URL;
	}
	
	public void setLocalPath(String LocalPath){
		this.LocalPath = LocalPath;
	}
	
	public void setURLSound(String URLSound) {
		this.URLSound = URLSound;
	}
	
	public void setDownloadedSound(String DownloadedSound) {
		this.DownloadedSound = DownloadedSound;
	}
	
	public void setLocalPathSound(String LocalPathSound) {
		this.LocalPathSound = LocalPathSound;
	}

	public String getCategory(){
		return Category;
	}
	
	public String getSection(){
		return Section;
	}
	
	public String getName(){
		return Name;
	}
	
	public String getDownloaded(){
		return Downloaded;
	}
	
	public String getURL(){
		return URL;
	}
	
	public String getLocalPath(){
		return LocalPath;
	}
	
	public String getURLSound(){
		return URLSound;
	}
	
	public String getDownloadedSound(){
		return DownloadedSound;
	}
	
	public String getLocalPathSound(){
		return LocalPathSound;
	}
}