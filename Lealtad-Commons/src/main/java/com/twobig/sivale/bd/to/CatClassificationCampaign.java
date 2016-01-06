<<<<<<< HEAD
package com.twobig.sivale.bd.to;
//Generated 14/12/2015 12:03:39 PM by Hibernate Tools 4.3.1



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
* CatClassificationCampaigns generated by hbm2java
*/
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name="cat_classification_campaigns"
 ,catalog="lealtad_schema"
)
public class CatClassificationCampaign  implements java.io.Serializable {


  private int catClassificationCampaignsId;
  private CatClassificationCampaign catClassificationCampaigns;
  private CatView catViews;
  private TCompany TCompanies;
  private String className;
  private String description;
  private int level;

 public CatClassificationCampaign() {
 }

	
 public CatClassificationCampaign(int catClassificationCampaignsId, TCompany TCompanies, String className) {
     this.catClassificationCampaignsId = catClassificationCampaignsId;
     this.TCompanies = TCompanies;
     this.className = className;
     this.level = level;
 }
 public CatClassificationCampaign(int catClassificationCampaignsId, CatClassificationCampaign catClassificationCampaigns, CatView catViews, TCompany TCompanies, String className, String description) {
    this.catClassificationCampaignsId = catClassificationCampaignsId;
    this.catClassificationCampaigns = catClassificationCampaigns;
    this.catViews = catViews;
    this.TCompanies = TCompanies;
    this.className = className;
    this.description = description;
    this.level = level;
 }

  @Id 

 
 @Column(name="cat_classification_campaigns_id", unique=true, nullable=false)
 public int getCatClassificationCampaignsId() {
     return this.catClassificationCampaignsId;
 }
 
 public void setCatClassificationCampaignsId(int catClassificationCampaignsId) {
     this.catClassificationCampaignsId = catClassificationCampaignsId;
 }

@ManyToOne(fetch=FetchType.LAZY)
 @JoinColumn(name="parent_classification_id")
 public CatClassificationCampaign getCatClassificationCampaigns() {
     return this.catClassificationCampaigns;
 }
 
 public void setCatClassificationCampaigns(CatClassificationCampaign catClassificationCampaigns) {
     this.catClassificationCampaigns = catClassificationCampaigns;
 }

@ManyToOne(fetch=FetchType.LAZY)
 @JoinColumn(name="fk_view")
 public CatView getCatViews() {
     return this.catViews;
 }
 
 public void setCatViews(CatView catViews) {
     this.catViews = catViews;
 }

@ManyToOne(fetch=FetchType.LAZY)
 @JoinColumn(name="fk_company", nullable=false)
 public TCompany getTCompanies() {
     return this.TCompanies;
 }
 
 public void setTCompanies(TCompany TCompanies) {
     this.TCompanies = TCompanies;
 }

 
 @Column(name="class_name", nullable=false, length=45)
 public String getClassName() {
     return this.className;
 }
 
 public void setClassName(String className) {
     this.className = className;
 }

 
 @Column(name="description", length=150)
 public String getDescription() {
     return this.description;
 }
 
 public void setDescription(String description) {
     this.description = description;
 }

 
 @Column(name="level", nullable=false)
 public int getLevel() {
     return this.level;
 }
 
 public void setLevel(int level) {
     this.level = level;
 }
=======
package com.twobig.sivale.bd.to;
//Generated 14/12/2015 12:03:39 PM by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * CatClassificationCampaigns generated by hbm2java
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "cat_classification_campaigns", catalog = "lealtad_schema")
public class CatClassificationCampaign implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4703036863978770613L;
	private int catClassificationCampaignsId;
	private Integer catClassificationCampaignsIdParent;
	private CatView catViews;
	private String className;
	private String description;
	private int level;

	public static final String FIELD_CAT_CLASSIFICATION_ID = "catClassificationCampaignsId";
	public static final String FIELD_CAT_CLASSIFICATION_LEVEL = "level";

	public CatClassificationCampaign() {
	}

	@Id

	@Column(name = "cat_classification_campaigns_id", unique = true, nullable = false)
	public int getCatClassificationCampaignsId() {
		return this.catClassificationCampaignsId;
	}

	public void setCatClassificationCampaignsId(int catClassificationCampaignsId) {
		this.catClassificationCampaignsId = catClassificationCampaignsId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_view")
	public CatView getCatViews() {
		return this.catViews;
	}

	public void setCatViews(CatView catViews) {
		this.catViews = catViews;
	}

	@Column(name = "class_name", nullable = false, length = 45)
	public String getClassName() {
		return this.className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Column(name = "description", length = 150)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "level", nullable = false)
	public int getLevel() {
		return this.level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Column(name = "parent_classification_id")
	public Integer getCatClassificationCampaignsIdParent() {
		return catClassificationCampaignsIdParent;
	}

	public void setCatClassificationCampaignsIdParent(Integer catClassificationCampaignsIdParent) {
		this.catClassificationCampaignsIdParent = catClassificationCampaignsIdParent;
	}

	@Override
	public String toString() {
		return "CatClassificationCampaign [catClassificationCampaignsId=" + catClassificationCampaignsId
				+ ", catClassificationCampaignsIdParent=" + catClassificationCampaignsIdParent + ", className="
				+ className + ", description=" + description + ", level=" + level + "]";
	}
>>>>>>> remotes/origin/Services
}