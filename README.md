# JsonValidationOverWSDL
Json-Schema generator based on WSDL definitions.

JsonValidationOverWSDL is library converts wsdl to json-schema.


WSDL: 
```xml
<?xml version="1.0" encoding="UTF-8"?>
<wsdl:definitions name="sample" targetNamespace="http://www.example.org/sample/" xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/" xmlns:tns="http://www.example.org/sample/" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/" xmlns:con="http://eviware.com/soapui/config">
  <wsdl:types>
    <xsd:schema targetNamespace="http://www.example.org/sample/">
      <xsd:element name="searchResponse">
        <xsd:complexType>
          <xsd:sequence>
            <xsd:element name="item" type="tns:ItemType"/>
          </xsd:sequence>
        </xsd:complexType>
      </xsd:element>
      <xsd:complexType name="ItemType">
        <xsd:sequence>
          <xsd:element name="id" type="xsd:string"/>
          <xsd:element name="description" type="xsd:string"/>
          <xsd:element name="price" type="xsd:string"/>
        </xsd:sequence>
      </xsd:complexType>
      <xsd:element name="buyResponse">
        <xsd:complexType>
          <xsd:sequence>
            <xsd:element name="purchasestatus" type="tns:PurchaseStatusType"/>
          </xsd:sequence>
        </xsd:complexType>
      </xsd:element>
      <xsd:complexType name="PurchaseStatusType">
        <xsd:sequence>
          <xsd:element name="id" type="xsd:string"/>
          <xsd:element name="stockStatus" type="xsd:string"/>
          <xsd:element name="expectedDelivery" type="xsd:string"/>
        </xsd:sequence>
      </xsd:complexType>
    </xsd:schema>
  </wsdl:types>
  <wsdl:message name="loginRequest">
    <wsdl:part name="username" type="xsd:string"/>
    <wsdl:part name="password" type="xsd:string"/>
  </wsdl:message>
  <wsdl:message name="loginResponse">
    <wsdl:part name="sessionid" type="xsd:string"/>
  </wsdl:message>
    <wsdl:message name="logoutResponse">
    <wsdl:part name="sessioninfo" type="xsd:string"/>
  </wsdl:message>
  <wsdl:message name="logoutRequest">
    <wsdl:part name="sessionid" type="xsd:string"/>
  </wsdl:message>
  <wsdl:message name="searchRequest">
    <wsdl:part name="sessionid" type="xsd:string"/>
    <wsdl:part name="searchstring" type="xsd:string"/>
  </wsdl:message>
  <wsdl:message name="searchResponse">
    <wsdl:part name="searchresult" element="tns:searchResponse"/>
  </wsdl:message>
  <wsdl:message name="buyRequest">
    <wsdl:part name="sessionid" type="xsd:string"/>
    <wsdl:part name="buystring" type="xsd:string"/>
  </wsdl:message>
  <wsdl:message name="buyResponse">
    <wsdl:part name="buyresult" element="tns:buyResponse"/>
  </wsdl:message>
  <wsdl:message name="login_faultMsg">
    <wsdl:part name="loginFault" type="xsd:string"/>
  </wsdl:message>
  <wsdl:message name="logout_faultMsg">
    <wsdl:part name="logoutFault" type="xsd:string"/>
  </wsdl:message>
  <wsdl:message name="search_faultMsg">
    <wsdl:part name="searchFault" type="xsd:string"/>
  </wsdl:message>
  <wsdl:message name="buy_faultMsg">
    <wsdl:part name="buyFault" type="xsd:string"/>
  </wsdl:message>
  <wsdl:portType name="ISampleService">
    <wsdl:operation name="login">
      <wsdl:input message="tns:loginRequest"/>
      <wsdl:output message="tns:loginResponse"/>
      <wsdl:fault name="fault" message="tns:login_faultMsg"/>
    </wsdl:operation>
    <wsdl:operation name="logout">
      <wsdl:input message="tns:logoutRequest"/>
      <wsdl:output message="tns:logoutResponse"/>
      <wsdl:fault name="fault" message="tns:logout_faultMsg"/>
    </wsdl:operation>
    <wsdl:operation name="search">
      <wsdl:input message="tns:searchRequest"/>
      <wsdl:output message="tns:searchResponse"/>
      <wsdl:fault name="fault" message="tns:search_faultMsg"/>
    </wsdl:operation>
    <wsdl:operation name="buy">
      <wsdl:input message="tns:buyRequest"/>
      <wsdl:output message="tns:buyResponse"/>
      <wsdl:fault name="fault" message="tns:buy_faultMsg"/>
    </wsdl:operation>
  </wsdl:portType>
  <wsdl:binding name="SampleServiceSoapBinding" type="tns:ISampleService">
    <soap:binding style="rpc" transport="http://schemas.xmlsoap.org/soap/http"/>
    <wsdl:operation name="login">
      <soap:operation soapAction="http://www.example.org/sample/login"/>
      <wsdl:input>
        <soap:body use="literal" namespace="http://www.example.org/sample/"/>
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" namespace="http://www.example.org/sample/"/>
      </wsdl:output>
      <wsdl:fault name="fault">
        <soap:fault use="literal" name="fault"/>
      </wsdl:fault>
    </wsdl:operation>
    <wsdl:operation name="logout">
      <soap:operation soapAction="http://www.example.org/sample/logout"/>
      <wsdl:input>
        <soap:body use="literal" namespace="http://www.example.org/sample/"/>
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" namespace="http://www.example.org/sample/"/>
      </wsdl:output>
      <wsdl:fault name="fault">
        <soap:fault use="literal" name="fault"/>
      </wsdl:fault>
    </wsdl:operation>
    <wsdl:operation name="search">
      <soap:operation soapAction="http://www.example.org/sample/search"/>
      <wsdl:input>
        <soap:body use="literal" namespace="http://www.example.org/sample/"/>
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" namespace="http://www.example.org/sample/"/>
      </wsdl:output>
      <wsdl:fault name="fault">
        <soap:fault use="literal" name="fault"/>
      </wsdl:fault>
    </wsdl:operation>
    <wsdl:operation name="buy">
      <soap:operation soapAction="http://www.example.org/sample/buy"/>
      <wsdl:input>
        <soap:body use="literal" namespace="http://www.example.org/sample/"/>
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" namespace="http://www.example.org/sample/"/>
      </wsdl:output>
      <wsdl:fault name="fault">
        <soap:fault use="literal" name="fault"/>
      </wsdl:fault>
    </wsdl:operation>
  </wsdl:binding>
  <wsdl:service name="SampleService">
    <wsdl:port name="SamplePort" binding="tns:SampleServiceSoapBinding">
      <soap:address location="http://www.soapui.org/sample"/>
    </wsdl:port>
  </wsdl:service>
</wsdl:definitions>
```

JSON-Schema : 
```javascript
{  
   "$schema":"http://json-schema.org/draft-04/schema#",
   "type":"object",
   "additionalProperties":false,
   "definitions":{  
      "PurchaseStatusType_CT":{  
         "type":"object",
         "additionalProperties":false,
         "id":"http://www.example.org/sample//PurchaseStatusType_CT",
         "properties":{  
            "id":{  
               "type":"string",
               "id":"http://www.example.org/sample//id"
            },
            "stockStatus":{  
               "type":"string",
               "id":"http://www.example.org/sample//stockStatus"
            },
            "expectedDelivery":{  
               "type":"string",
               "id":"http://www.example.org/sample//expectedDelivery"
            }
         },
         "required":[  
            "id",
            "stockStatus",
            "expectedDelivery"
         ]
      },
      "ItemType_CT":{  
         "type":"object",
         "additionalProperties":false,
         "id":"http://www.example.org/sample//ItemType_CT",
         "properties":{  
            "id":{  
               "type":"string",
               "id":"http://www.example.org/sample//id"
            },
            "description":{  
               "type":"string",
               "id":"http://www.example.org/sample//description"
            },
            "price":{  
               "type":"string",
               "id":"http://www.example.org/sample//price"
            }
         },
         "required":[  
            "id",
            "description",
            "price"
         ]
      },
      "searchResponseembedded":{  
         "type":"object",
         "additionalProperties":false,
         "id":"http://www.example.org/sample//searchResponseembedded",
         "properties":{  
            "item":{  
               "$ref":"#/definitions/ItemType_CT"
            }
         },
         "required":[  
            "item"
         ]
      },
      "buyResponseembedded":{  
         "type":"object",
         "additionalProperties":false,
         "id":"http://www.example.org/sample//buyResponseembedded",
         "properties":{  
            "purchasestatus":{  
               "$ref":"#/definitions/PurchaseStatusType_CT"
            }
         },
         "required":[  
            "purchasestatus"
         ]
      }
   },
   "properties":{  
      "username":{  
         "type":"string",
         "id":"wsdl.namespace/username"
      },
      "password":{  
         "type":"string",
         "id":"wsdl.namespace/password"
      },
      "sessionid":{  
         "type":"string",
         "id":"wsdl.namespace/sessionid"
      },
      "sessioninfo":{  
         "type":"string",
         "id":"wsdl.namespace/sessioninfo"
      },
      "searchstring":{  
         "type":"string",
         "id":"wsdl.namespace/searchstring"
      },
      "searchResponse":{  
         "$ref":"#/definitions/searchResponseembedded"
      },
      "buystring":{  
         "type":"string",
         "id":"wsdl.namespace/buystring"
      },
      "buyResponse":{  
         "$ref":"#/definitions/buyResponseembedded"
      }
   }
}
```

Usage : 

```java
package com.devside.esb.json.model;

import junit.framework.TestCase;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.json.JSONObject;

import com.devside.esb.json.model.JSONSchemaGenerator;
import com.devside.esb.json.model.TypeSchema;

public class SampleServiceTest extends TestCase {

	Schema schema;
	protected void setUp(){
		schema = JSONSchemaGenerator.generate(TypeSchema.class.getClassLoader().getResource("sample-service.wsdl").getPath());
	}
	
	public void testEchoSuccess(){
		schema.validate(new JSONObject("{\"username\":\"user01\",\"password\":\"pass01\"}"));
	}
	
	public void testEchoAdditionalField(){
		try {
			schema.validate(new JSONObject("{\"username\":\"user01\",\"password\":\"pass01\",\"falan\":\"filan\"}"));
			assertTrue(false);
		} catch (Exception e) {
			assertTrue(e instanceof ValidationException);
		}
		
		
	}
}



```


