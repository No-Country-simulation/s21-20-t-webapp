package com.inventario.demo.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.servers.ServerVariable;
import org.springframework.http.HttpHeaders;

@OpenAPIDefinition(
        info = @Info(
                title = "Inventory Management SaaS API",
                description = "API SaaS para gestión avanzada de inventarios multiempresa con control de productos, almacenes, proveedores y seguimiento en tiempo real",
                termsOfService = "https://www.inventory-saas.com/terms",
                version = "2.1.0",
                contact = @Contact(
                        name = "SaaS Support Team",
                        url = "https://support.inventory-saas.com",
                        email = "techsupport@inventory-saas.com"
                ),
                license = @License(
                        name = "Apache License 2.0 for Inventory SaaS",
                        url = "https://www.apache.org/licenses/LICENSE-2.0",
                        identifier = "Apache-2.0"
                )
        ),
        servers = {
                @Server(
                        description = "Local Development",
                        url = "http://localhost:8080",
                        variables = {
                                @ServerVariable(
                                        name = "tenantId",
                                        defaultValue = "demo",
                                        allowableValues = {"dev", "test", "demo"},
                                        description = "ID de tenant para desarrollo"
                                )
                        }
                ),
                @Server(
                        description = "Staging Environment",
                        url = "https://staging.inventory-saas.com",
                        variables = {
                                @ServerVariable(
                                        name = "tenantId",
                                        defaultValue = "test-tenant",
                                        description = "ID de tenant para pruebas"
                                )
                        }
                ),
                @Server(
                        description = "Production Cluster EU",
                        url = "https://eu.api.inventory-saas.com",
                        variables = {
                                @ServerVariable(
                                        name = "tenantId",
                                        defaultValue = "default-tenant",
                                        description = "ID de tenant específico"
                                )
                        }
                ),
                @Server(
                        description = "Production Cluster US",
                        url = "https://us.api.inventory-saas.com",
                        variables = {
                                @ServerVariable(
                                        name = "tenantId",
                                        description = "ID de tenant específico",
                                        defaultValue = "default-tenant"
                                )
                        }
                )
        }
        /*security = @SecurityRequirement(
                name = "securityToken"
        )*/
)
@SecurityScheme(
        name = "securityToken",
        description = "Access Token For My API",
        type = SecuritySchemeType.HTTP,
        paramName = HttpHeaders.AUTHORIZATION,
        in = SecuritySchemeIn.HEADER,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {

}
