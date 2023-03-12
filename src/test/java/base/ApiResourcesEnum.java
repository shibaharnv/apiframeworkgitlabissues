package base;

public enum ApiResourcesEnum {

	subscribe("/subscribe"),
	unsubscribe("/unsubscribe"),
	move("/move"),
	clone("/clone");
	
	private String resource;
	
	ApiResourcesEnum(String resource)
	{
		this.resource =resource;
	}
	
	
	
	public String getresourcemethod()
	{
		return resource;
	}
	
	

}
