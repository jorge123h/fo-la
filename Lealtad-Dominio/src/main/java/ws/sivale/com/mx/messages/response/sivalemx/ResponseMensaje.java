/**
 * ResponseMensaje.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ws.sivale.com.mx.messages.response.sivalemx;

public class ResponseMensaje  extends ws.sivale.com.mx.messages.response.sivalemx.ResponseBase  implements java.io.Serializable {
    private java.lang.String mensaje;

    private ws.sivale.com.mx.messages.response.ResponseEstadisticas estadisticas;

    public ResponseMensaje() {
    }

    public ResponseMensaje(
           ws.sivale.com.mx.messages.response.ResponseError responseError,
           java.lang.String mensaje,
           ws.sivale.com.mx.messages.response.ResponseEstadisticas estadisticas) {
        super(
            responseError);
        this.mensaje = mensaje;
        this.estadisticas = estadisticas;
    }


    /**
     * Gets the mensaje value for this ResponseMensaje.
     * 
     * @return mensaje
     */
    public java.lang.String getMensaje() {
        return mensaje;
    }


    /**
     * Sets the mensaje value for this ResponseMensaje.
     * 
     * @param mensaje
     */
    public void setMensaje(java.lang.String mensaje) {
        this.mensaje = mensaje;
    }


    /**
     * Gets the estadisticas value for this ResponseMensaje.
     * 
     * @return estadisticas
     */
    public ws.sivale.com.mx.messages.response.ResponseEstadisticas getEstadisticas() {
        return estadisticas;
    }


    /**
     * Sets the estadisticas value for this ResponseMensaje.
     * 
     * @param estadisticas
     */
    public void setEstadisticas(ws.sivale.com.mx.messages.response.ResponseEstadisticas estadisticas) {
        this.estadisticas = estadisticas;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ResponseMensaje)) return false;
        ResponseMensaje other = (ResponseMensaje) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.mensaje==null && other.getMensaje()==null) || 
             (this.mensaje!=null &&
              this.mensaje.equals(other.getMensaje()))) &&
            ((this.estadisticas==null && other.getEstadisticas()==null) || 
             (this.estadisticas!=null &&
              this.estadisticas.equals(other.getEstadisticas())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getMensaje() != null) {
            _hashCode += getMensaje().hashCode();
        }
        if (getEstadisticas() != null) {
            _hashCode += getEstadisticas().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ResponseMensaje.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://mx.com.sivale.ws/messages/response/sivalemx", "ResponseMensaje"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mensaje");
        elemField.setXmlName(new javax.xml.namespace.QName("", "mensaje"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("estadisticas");
        elemField.setXmlName(new javax.xml.namespace.QName("", "estadisticas"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://mx.com.sivale.ws/messages/response", "ResponseEstadisticas"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
