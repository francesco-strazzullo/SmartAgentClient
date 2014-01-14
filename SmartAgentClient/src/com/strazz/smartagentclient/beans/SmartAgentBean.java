package com.strazz.smartagentclient.beans;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;

import com.google.gson.Gson;

@ManagedBean
@ViewScoped
public class SmartAgentBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Gson GSON = new Gson();

	private List<String> paths = new ArrayList<String>(0);
	private String currentContent;
	private String currentPath;

	public void onListLoad() {
		String jsonData = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("list");
		List<String> paths = GSON.fromJson(jsonData, List.class);
		
		
		paths = (List<String>) CollectionUtils.collect(paths, new Transformer() {
			public Object transform(Object input) {
				return StringUtils.substringAfterLast(input.toString(), File.separator);
			}
		});
		
		this.setPaths(paths);
	}
	
	public void onGet(){
		
		
		String base64Content = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("content");
		this.setCurrentContent(new String(Base64.decodeBase64(base64Content.getBytes())));
		
		this.setCurrentPath((String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("path"));
	}
	
	public void create(){
		this.currentContent = StringUtils.EMPTY;
		this.currentPath = new Date().getTime() + ".html";
	}
	
	public void save(){
		RequestContext.getCurrentInstance().execute("save('" + currentPath + "','" + Base64.encodeBase64String(currentContent.getBytes()) + "');");
	}

	public List<String> getPaths() {
		return paths;
	}

	public void setPaths(List<String> paths) {
		this.paths = paths;
	}

	public String getCurrentContent() {
		return currentContent;
	}

	public void setCurrentContent(String currentContent) {
		this.currentContent = currentContent;
	}
	
	public String getCurrentPath() {
		return currentPath;
	}

	public void setCurrentPath(String currentPath) {
		this.currentPath = currentPath;
	}

	public String encodeParameter(String parameter){
		try {
			return URLEncoder.encode(parameter, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

}
