package com.twobig.sivale.bd.to;// Generated 14/12/2015 01:25:30 PM by Hibernate Tools 4.3.1

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * TPublications generated by hbm2java
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "t_publications")
public class TPublication implements java.io.Serializable {

	private static final long serialVersionUID = -1260077479616668004L;
	private int publicationId;
	private CatPublicationType catPublicationType;
	private Integer tCampaignId;
	private Date publishedDate;
	private String name;
	private String templateFilePath;
	private String dataFilePath;
	private String dataFilePage;
	private String description;
	private Boolean isEnable;
	private String imagePath;

	public static final String FIELD_PUBLICATION_ID = "publicationId";
	public static final String FIELD_CAMPAIGN_ID = "tCampaignId";
	public static final String FIELD_IS_ENABLE = "isEnable";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "publication_id")
	@SequenceGenerator(name = "publication_id", sequenceName = "PUBLICATION_SEQ", initialValue = 0, allocationSize = 0)
	@Column(name = "publication_id", unique = true, nullable = false)
	public int getPublicationId() {
		return this.publicationId;
	}

	public void setPublicationId(int publicationId) {
		this.publicationId = publicationId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fk_publication_type")
	public CatPublicationType getCatPublicationType() {
		return this.catPublicationType;
	}

	public void setCatPublicationType(CatPublicationType catPublicationType) {
		this.catPublicationType = catPublicationType;
	}

	@Column(name = "fk_campaign")
	public Integer gettCampaignId() {
		return tCampaignId;
	}

	public void settCampaignId(Integer tCampaignId) {
		this.tCampaignId = tCampaignId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "published_date", length = 19)
	public Date getPublishedDate() {
		return this.publishedDate;
	}

	public void setPublishedDate(Date publishedDate) {
		this.publishedDate = publishedDate;
	}

	@Column(name = "name", length = 45)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "template_file_path", length = 45)
	public String getTemplateFilePath() {
		return this.templateFilePath;
	}

	public void setTemplateFilePath(String templateFilePath) {
		this.templateFilePath = templateFilePath;
	}

	@Column(name = "data_file_path", length = 45)
	public String getDataFilePath() {
		return this.dataFilePath;
	}

	public void setDataFilePath(String dataFilePath) {
		this.dataFilePath = dataFilePath;
	}

	@Column(name = "data_file_page", length = 45)
	public String getDataFilePage() {
		return this.dataFilePage;
	}

	public void setDataFilePage(String dataFilePage) {
		this.dataFilePage = dataFilePage;
	}

	@Column(name = "description", length = 150)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "is_enable")
	public Boolean getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Boolean isEnable) {
		this.isEnable = isEnable;
	}

	@Column(name = "image_file_path", length = 45)
	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	@Override
	public String toString() {
		return "TPublication [publicationId=" + publicationId + ", publishedDate=" + publishedDate + ", name=" + name
				+ ", description=" + description + "]";
	}
}