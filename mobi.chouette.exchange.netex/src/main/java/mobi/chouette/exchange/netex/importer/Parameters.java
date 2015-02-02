package mobi.chouette.exchange.netex.importer;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import mobi.chouette.exchange.parameters.AbstractParameter;
import mobi.chouette.exchange.validation.parameters.ValidationParameters;


@XmlType (name= "netexImport")
public class Parameters  extends AbstractParameter{

	private Boolean noSave;
	
	private ValidationParameters validation;

	/**
	 * @return the noSave
	 */
	@XmlAttribute(name = "no_save")
	public Boolean getNoSave() {
		return noSave;
	}

	/**
	 * @param noSave the noSave to set
	 */
	public void setNoSave(Boolean noSave) {
		this.noSave = noSave;
	}

	/**
	 * @return the validation
	 */
	@XmlAttribute(name = "validation")
	public ValidationParameters getValidation() {
		return validation;
	}

	/**
	 * @param validation the validation to set
	 */
	public void setValidation(ValidationParameters validation) {
		this.validation = validation;
	}
}