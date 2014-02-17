package tools;

import extras.Subsection;

public class ClickCategories{

private Subsection subsection;
private String Level;
private String SectionName;
	
	public ClickCategories(Subsection subsection, String Level, String SectionName) {
		this.subsection = subsection;
		this.Level = Level;
		this.SectionName = SectionName;
	}
	
	public void doClick(){
		subsection.setInformationToList(Level, SectionName);
	}
}