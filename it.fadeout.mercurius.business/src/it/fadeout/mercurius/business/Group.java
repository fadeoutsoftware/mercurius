package it.fadeout.mercurius.business;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Mercurius Group
 * @author p.campanella
 *
 */
@Entity
@Table(name="groups")
@XmlRootElement
public class Group {
	
	@Id
	@GeneratedValue
	@Column(name="idgroup")
	int IdGroup;
	
	@Column(name="groupname")
	String GroupName;
	
	@ManyToMany(cascade = CascadeType.ALL , fetch = FetchType.EAGER)
	@JoinTable(name="contactsgroups" , joinColumns = { @JoinColumn(name = "idgroup") }, inverseJoinColumns = { @JoinColumn(name = "idcontact") })
	Set<Contact> members = new HashSet<Contact>(0);

	public int getIdGroup() {
		return IdGroup;
	}

	public void setIdGroup(int idGroup) {
		IdGroup = idGroup;
	}

	public String getGroupName() {
		return GroupName;
	}

	public void setGroupName(String groupName) {
		GroupName = groupName;
	}

	public Set<Contact> getMembers() {
		return members;
	}

	public void setMembers(Set<Contact> members) {
		this.members = members;
	}

	
}
