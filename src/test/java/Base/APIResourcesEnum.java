package Base;

public enum APIResourcesEnum {
	
	addplaceApi("/maps/api/place/add/json"),
	deleteplaceApi("/maps/api/place/delete/json"),
	getplaceApi("/maps/api/place/get/json");
	
	//maps/api/place/get/json
	
	private String resource;
	
	APIResourcesEnum(String resource)
	{
		this.resource =resource;
	}
	
	
	
	public String getresourcemethod()
	{
		return resource;
	}
	
	

}
