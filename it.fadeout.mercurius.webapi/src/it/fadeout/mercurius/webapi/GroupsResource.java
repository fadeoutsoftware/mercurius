package it.fadeout.mercurius.webapi;

import it.fadeout.mercurius.business.Group;
import it.fadeout.mercurius.business.PrimitiveResult;
import it.fadeout.mercurius.data.GroupsRepository;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/groups")
public class GroupsResource {
	@GET
	@Produces({"application/xml", "application/json", "text/xml"})
	@Path("/{idgroup}")
	public Group getGroup(@PathParam("idgroup") int idGroup) {		
		GroupsRepository oGroupRepository = new GroupsRepository();
		Group oGroup = oGroupRepository.Select(idGroup, Group.class);
		return oGroup;
	}
	
	@GET
	@Produces({"application/xml", "application/json", "text/xml"})	
	@Path("/all")
	public List<Group> getGroups() {
		GroupsRepository oGroupsRepository = new GroupsRepository();
		List<Group> aoGroups = oGroupsRepository.SelectAll(Group.class);
		return aoGroups;
	}	
	
	@PUT
	@Produces({"application/xml", "application/json", "text/xml"})	
	@Consumes({"application/xml", "application/json", "text/xml"})
	public PrimitiveResult saveGroup(Group oGroup) {
		PrimitiveResult oResult = new PrimitiveResult();
		oResult.IntValue = -1;
		
		if (oGroup == null) return oResult;
		
		GroupsRepository oGroupsRepository = new GroupsRepository();
		oGroupsRepository.Save(oGroup);
		int iIdGroup = oGroup.getIdGroup();
		
		oResult.IntValue = iIdGroup;
		return oResult;
	}
	
	@DELETE
	@Produces({"application/xml", "application/json", "text/xml"})
	@Path("/{idcontact}")
	public PrimitiveResult deleteGroup(@PathParam("idgroup") int idGroup) {
		PrimitiveResult oResult = new PrimitiveResult();
		GroupsRepository oGroupsRepository = new GroupsRepository();
		Group oGroup = oGroupsRepository.Select(idGroup, Group.class);
		if (oGroup == null) {
			oResult.BoolValue = false;
			return oResult;
		}
		else {
			
			oResult.BoolValue = oGroupsRepository.Delete(oGroup);
			return oResult;
		}
	}	
	
	
	@POST
	@Produces({"application/xml", "application/json", "text/xml"})	
	@Consumes({"application/xml", "application/json", "text/xml"})
	public PrimitiveResult updateGroup(Group oGroup) {
		PrimitiveResult oResult = new PrimitiveResult();
		oResult.BoolValue = false;
		
		if (oGroup == null) return oResult;
		
		GroupsRepository oContactRepository = new GroupsRepository();
		oResult.BoolValue = oContactRepository.Update(oGroup);
		
		return oResult;
	}	

}
