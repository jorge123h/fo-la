package com.twobig.sivale.bd.to;
// Generated 20/11/2015 02:09:49 PM by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * TAttachedFiles generated by hbm2java
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "t_attached_files", catalog = "bolinf")
public class TAttachedFile implements java.io.Serializable {

	private static final long serialVersionUID = -2458367360511518602L;
	private Integer attachedFileId;
	private Integer tPublicationId;
	private Boolean isPublic;
	private Boolean isAcuse; 
	private String fileName;
	private String filePath;
	private String fileExtension;
	private Integer tCampaignId; 

	public static final String FIELD_TPUBLICATION_ID = "tPublicationId";
	public static final String FIELD_TCAMPAIGN_ID = "tCampaignId";  

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attached_file_id")
	@SequenceGenerator(name = "attached_file_id", sequenceName = "ATTACHED_FILE_SEQ", initialValue = 0, allocationSize = 0)
	@Column(name = "attached_file_id", unique = true, nullable = false)
	public Integer getAttachedFileId() {
		return this.attachedFileId;
	}

	public void setAttachedFileId(Integer attachedFileId) {
		this.attachedFileId = attachedFileId;
	}

	@Column(name = "fk_publication")
	public Integer gettPublicationId() {
		return tPublicationId;
	}

	public void settPublicationId(Integer tPublicationId) {
		this.tPublicationId = tPublicationId;
	}

	@Column(name = "is_public")
	public Boolean getIsPublic() {
		return this.isPublic;
	}

	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}

	@Column(name = "file_name", length = 45)
	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "file_path", length = 45)
	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Column(name = "file_extension", length = 45)
	public String getFileExtension() {
		return this.fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	@Column(name = "fk_campaign")
	public Integer gettCampaignId() {
		return tCampaignId;
	}

	public void settCampaignId(Integer tCampaignId) {
		this.tCampaignId = tCampaignId;
	}
	
	@Column(name = "is_acuse")
	public Boolean getIsAcuse() {
		return isAcuse;
	}

	public void setIsAcuse(Boolean isAcuse) {
		this.isAcuse = isAcuse;
	}

	@Override
	public String toString() {
		return "TAttachedFile [attachedFileId=" + attachedFileId + ", tPublicationId=" + tPublicationId + ", isPublic="
				+ isPublic + ", fileName=" + fileName + ", filePath=" + filePath + ", fileExtension=" + fileExtension
				+ ", tCampaignId=" + tCampaignId + "]";
	}
	
}
