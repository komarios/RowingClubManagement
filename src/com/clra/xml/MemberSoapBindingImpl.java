/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: MemberSoapBindingImpl.java,v $
 * $Date: 2003/03/01 00:48:54 $
 * $Revision: 1.3 $
 */

package com.clra.xml;

import com.clra.member.Address;
import com.clra.member.Email;
import com.clra.member.MemberDBRead;
import com.clra.member.MemberName;
import com.clra.member.MemberRole;
import com.clra.member.MemberSnapshot;
import com.clra.member.Telephone;
import com.clra.xml.beans.AddressBean;
import com.clra.xml.beans.MemberBean;
import com.clra.xml.beans.MemberNameBean;
import com.clra.xml.beans.MemberRoleBean;
import java.util.Collection;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import org.apache.log4j.Category;

/**
 * This file was auto-generated from WSDL by the Apache Axis WSDL2Java emitter,
 * and then modified by hand.
 * @version $Revision: 1.3 $  ($Date: 2003/03/01 00:48:54 $)
 * @author <a href="mailto:rphall@pluto.njcc.com">Rick Hall</a>
 */
public class MemberSoapBindingImpl implements com.clra.xml.IMemberXmlRpc{

    private final static String base = MemberSoapBindingImpl.class.getName();
    private final static Category theLog = Category.getInstance( base );

    public com.clra.xml.beans.MemberBean[] findAllMembers()
      throws java.rmi.RemoteException {

        MemberSnapshot[] memberSnapshots = new MemberSnapshot[0];
        try {
          Collection c = MemberDBRead.findAllMembersByLastName();
          memberSnapshots =
              (MemberSnapshot[]) c.toArray( new MemberSnapshot[0] );
        }
        catch( Exception x ) {
          // This is ugly. Should generate a wsdl:fault instead.
          theLog.error( x );
        }

        final int MAX = memberSnapshots.length;
        MemberBean[] retVal = new MemberBean[ MAX ];
        for ( int i=0; i<MAX; i++ ) {

          MemberSnapshot ms = memberSnapshots[i];

          retVal[i] = new MemberBean();

          retVal[i].setId( ms.getId() );
          retVal[i].setAccountName( ms.getAccountName() );
          retVal[i].setAccountType( ms.getAccountType().toString() );

          Calendar accountDate = Calendar.getInstance();
          accountDate.setTime( ms.getAccountDate() );
          retVal[i].setAccountDate( accountDate );

          MemberName memberName = ms.getMemberName();
          MemberNameBean memberNameBean = new MemberNameBean();
          memberNameBean.setFirstName( memberName.getFirstName() );
          memberNameBean.setLastName( memberName.getLastName() );
          memberNameBean.setMiddleName( memberName.getMiddleName() );
          memberNameBean.setSuffix( memberName.getSuffix() );
          retVal[i].setMemberNameBean( memberNameBean );

          Address address = ms.getAddress();
          AddressBean addressBean = new AddressBean();
          addressBean.setStreet1( address.getStreet1() );
          addressBean.setStreet2( address.getStreet2() );
          addressBean.setCity( address.getCity() );
          addressBean.setState( address.getState() );
          addressBean.setZip( address.getZip() );
          retVal[i].setAddressBean( addressBean );

          Map phoneNumbers = ms.getTelephoneNumbers();
          Telephone phone = (Telephone) phoneNumbers.get( Telephone.EVENING );
          retVal[i].setEveningTelephone( phone.toString() );

          phone = (Telephone) phoneNumbers.get( Telephone.DAY );
          if ( phone != null ) {
            retVal[i].setDayTelephone( phone.toString() );
          }

          phone = (Telephone) phoneNumbers.get( Telephone.OTHER );
          if ( phone != null ) {
            retVal[i].setOtherTelephone( phone.toString() );
          }

          Email email = ms.getEmail();
          if ( email != null ) {
            retVal[i].setEmail( email.toString() );
          }

          MemberRole[] roles = ms.getMemberRoles();
          MemberRoleBean[] roleBeans = new MemberRoleBean[ roles.length ];
          for ( int j=0; j<roleBeans.length; j++ ) {
            roleBeans[j] = new MemberRoleBean();
            roleBeans[j].setRole( roles[j].getRole() );
            roleBeans[j].setRoleGroup( roles[j].getRoleGroup() );
          }
          retVal[i].setMemberRoleBeans( roleBeans );

          Calendar birthDate = Calendar.getInstance();
          Date d = ms.getBirthDate();
          if ( d != null ) {
            birthDate.setTime( ms.getBirthDate() );
            retVal[i].setBirthDate( birthDate );
          }

        } // for

        return retVal;
    } // findAllMembers()

} // MemberSoapBindingImpl

/*
 * $Log: MemberSoapBindingImpl.java,v $
 * Revision 1.3  2003/03/01 00:48:54  rphall
 * Removed import of TelephoneBean
 *
 * Revision 1.2  2003/02/28 20:28:45  rphall
 * XML-related file that is not auto-generated
 *
 * Revision 1.1  2003/02/26 20:17:57  rphall
 * XML-RPC functionality
 *
 */

