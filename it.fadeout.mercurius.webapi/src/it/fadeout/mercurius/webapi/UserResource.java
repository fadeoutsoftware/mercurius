package it.fadeout.mercurius.webapi;

import it.fadeout.mercurius.business.PrimitiveResult;
import it.fadeout.mercurius.data.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

@Path("/auth")
public class UserResource {
	private String m_sCorsHeaders;
	
	@GET
	@Produces({"application/xml", "application/json", "text/xml"})
	public PrimitiveResult Login(@Context HttpServletRequest oRequest) {
		PrimitiveResult oResult = new PrimitiveResult();
		oResult.BoolValue = false;
		
		String sUser = oRequest.getHeader("X-Mercurius-user");
		String sPassword = oRequest.getHeader("X-Mercurius-pwd");
		
		UserRepository oUserRepository = new UserRepository();
		oResult.BoolValue = oUserRepository.Login(sUser, sPassword);
		
		return oResult;
	}
	
	private Response makeCORS(ResponseBuilder oRequestBuilder) {
		   return makeCORS(oRequestBuilder, m_sCorsHeaders);
		}
	private Response makeCORS(ResponseBuilder req, String returnMethod) {
		   ResponseBuilder oRequestBuilder = req.header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");

		   if (!"".equals(returnMethod)) {
		      oRequestBuilder.header("Access-Control-Allow-Headers", returnMethod);
		   }

		   return oRequestBuilder.build();
		}
	
	 @OPTIONS
	 @Path("/contacts")
	 public Response corsMyResource(@HeaderParam("Access-Control-Request-Headers") String requestH) {
		 m_sCorsHeaders = requestH;
	     return makeCORS(Response.ok(), requestH);
	 }
	 
	 @OPTIONS
	 public Response corsMyResourcePut(@HeaderParam("Access-Control-Request-Headers") String requestH) {
		 m_sCorsHeaders = requestH;
	     return makeCORS(Response.ok(), requestH);
	 }
}
