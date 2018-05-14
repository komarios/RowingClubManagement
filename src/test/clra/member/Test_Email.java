/*
 * Copyright (c) Carnegie Lake Rowing Association 2002. All rights reserved.
 * Distributed under the GPL license. See doc/COPYING.
 * $RCSfile: Test_Email.java,v $
 * $Date: 2003/02/26 03:38:47 $
 * $Revision: 1.2 $
 */

package test.clra.member;

import com.clra.member.Email;
import junit.framework.TestCase;
import org.apache.log4j.Category;

/**
 * @version $Default:$ $Date: 2003/02/26 03:38:47 $
 */
public class Test_Email extends TestCase {

  // The logger used by this class
  private final Category theLog = Category.getInstance(Test_Email.class);

  public static void main( String[] args ) {

    new test.Log4jConfigurator("");

    Test_Email test = new Test_Email( "command-line test" );

    test.testIsValidEmail();
    test.testEquals();
    test.testHashCode();
    test.testHugeNumberOfEmails();

    return;
  } // main

  public Test_Email( String name ) {
    super( name );
  }

  public void setUp() {
  } // setUp()

  public void tearDown() {
  } // tearDown()

  public void testIsValidStart() {
  }

  public void testIsValidEnd() {
  }

  public void testIsValidLocalChar() {
  }

  public void testIsValidDomainChar() {
  }

  public void testIsValidEmail() {

    String email = "hsteam@briar.nj.abc.com";
    assertTrue( email, Email.isValidEmail(email) );

    email = "hsteam@res-arch.nj.abc.com";
    assertTrue( email, Email.isValidEmail(email) );

    email = "hs.eam@briar.nj.abc.com";
    assertTrue( email, Email.isValidEmail(email) );

    email = "hs_eam@briar.nj.abc.com";
    assertTrue( email, Email.isValidEmail(email) );

    email = "hs-eam@briar.nj.abc.com";
    assertTrue( email, Email.isValidEmail(email) );

    email = "hsteam@briar.nj.abc.com.";
    assertTrue( email, !Email.isValidEmail(email) );

    email = "hsteam@briar.nj.abc.com-";
    assertTrue( email, !Email.isValidEmail(email) );

    email = "hsteam@.briar.nj.abc.com";
    assertTrue( email, !Email.isValidEmail(email) );

    email = "hsteam@-briar.nj.abc.com";
    assertTrue( email, !Email.isValidEmail(email) );

    email = "hsteae.@briar.nj.abc.com";
    assertTrue( email, !Email.isValidEmail(email) );

    email = ".hsteam@briar.nj.abc.com";
    assertTrue( email, !Email.isValidEmail(email) );

  } // testIsValidEmail()

  public void testEquals() {

    String strEmail1 = "hsteam@briar.nj.abc.com";
    String strEmail2 = "HSTEAM@bRiAr.Nj.AbC.cOm";
    String strEmail3 = "hstea3@briar.nj.abc.com";

    Email email1 = new Email( strEmail1 );
    Email email2 = new Email( strEmail2 );
    Email email3 = new Email( strEmail3 );

    assertTrue( strEmail1+" == "+strEmail2, email1.equals(email2) );
    assertTrue( strEmail1+" != "+strEmail3, !email1.equals(email3) );
    assertTrue( strEmail2+" != "+strEmail3, !email2.equals(email3) );

  }

  public void testHashCode() {

    String strEmail1 = "hsteam@briar.nj.abc.com";
    String strEmail2 = "HSTEAM@bRiAr.Nj.AbC.cOm";
    String strEmail3 = "hstea3@briar.nj.abc.com";

    Email email1 = new Email( strEmail1 );
    Email email2 = new Email( strEmail2 );
    Email email3 = new Email( strEmail3 );

    assertTrue( "Hashcode "+strEmail1+" == "+strEmail2,
      email1.hashCode() == email2.hashCode() );

    assertTrue( "Hashcode "+strEmail1+" != "+strEmail3,
      email1.hashCode() != email3.hashCode() );

    assertTrue( "Hashcode "+strEmail2+" != "+strEmail3,
      email2.hashCode() != email3.hashCode() );

  }

  public void testHugeNumberOfEmails() {

    // Based on real addresses with non-recoverable scramblings

    String str = "kabao322@hdsuail.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "habayuic@poisu.jcj.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "idj@ebucawu.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "kiwiuscuhica34@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "odbbi.alaxacuao@uep.dom";
    assertTrue( str, Email.isValidEmail(str) );

    str = "aacualei@hdsuail.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "paoliauacs08@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "kasoau@hdsuail.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "cbakao@poiceasdc.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ascsbaao01@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "Jdc.Baosalu@vaoizdc.com";
    assertTrue( str, Email.isValidEmail(str) );

    str = "obasehaluao@auoa.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uaoypas.baassia@edceaos.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ujbaassia@ass.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "kas194@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "yackivao@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "b2msc@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "bdcsdsxwsshoieh@eu.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ualacia.ssekao@ass.com";
    assertTrue( str, Email.isValidEmail(str) );

    str = "jbdocu@auu.ass.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "obdslas@uuc.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "o92655@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "phdaba27@hdua.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "jaybiou78@hdsuail.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "edop888@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "aeaoeamcd@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uhaapmiol1@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "meaoeamcd@uashauasiea-upo.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "pas@ueee.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "eacuisu@uicuupoicm.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "simaolsv0@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "faccy@poiceasdc.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "aeliuhau@aaoshlick.com";
    assertTrue( str, Email.isValidEmail(str) );

    str = "mcduamol22@jscd.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "o.edlbsoc@iaaa.dom";
    assertTrue( str, Email.isValidEmail(str) );

    str = "auaaumsy@yahdd.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "daouuac14@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "udhaash@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "eddlaac@auail.uuc.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "jedp@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "aeduuaou@yahdd.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "pvduuusf@oec.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "wedsoscay@cjaua.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "wedsoscay@cjaua.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "wedsoscay@cjaua.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "juau@poiceasdc.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "jacu@ueisae.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uouaiseh@uicuupoicm.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ulaci@uicuupoicm.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "juavlic@hdua.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uabuiuamlid@jscd.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "auiueis9@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uuof123@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "bu@poiceasdc.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uwyaouac@hdua.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "jalliu@auaoieacbdyehdio.dom";
    assertTrue( str, Email.isValidEmail(str) );

    str = "kalliu@netuauia.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "acm@poiceasdc.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "jwfaokau@yahdd.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "iacf@pdbdx.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ofaocacuaz@oduassapaoscaou.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "cfaooaioa@blddubaom.com";
    assertTrue( str, Email.isValidEmail(str) );

    str = "valff7@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "lufiuehassi@comueapa.com";
    assertTrue( str, Email.isValidEmail(str) );

    str = "flyccw@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "flyccjw@be.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "bill.mdufoay@udwjdcau.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uydewm@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "hda6@uaileisy.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "mdubdla@hdua.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "pmddubduy@alsuci.williauu.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "sacss98@ix.comedu.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "jsliacmdoalli@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "jspajm@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "jaffm73@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "cmsekaos@udlbid.poiceasdc.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "msliekb1@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ophall@plssd.cjee.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ujhaluay@poiceasdc.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uhacauac@e3ieaoa.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "haoscoms@iueu.ossmaou.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "hipuac1@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "pasl.hayuac@uokw.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "phaabick@caludceduu.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "shaabick@swalsuci.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ehoiusiacmhacoy@hdsuail.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uhilsdc@poiceasdcicuiuao.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "lhdumkic@lawoaceavilla.dom";
    assertTrue( str, Email.isValidEmail(str) );

    str = "odbich39@hdsuail.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uu00379@yahdd.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "upaauylh@aaoshlick.com";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ohdcusaic@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ohdcusaic@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "lpahdpk@uusp.lpu.usasa.cj.su";
    assertTrue( str, Email.isValidEmail(str) );

    str = "suh74@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "bbaohscs@caoe.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "hsolayice@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "soaekal@uuc.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "hsua@poiceasdc.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "hsua@poiceasdc.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "icmaoulav@pbwdolu.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ajaedbudc9@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "wwphueoaw@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ujaoviu@phaouacom-eod.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "pasao_j_jacuac@uaoek.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "bjdhcudc@ucip.com";
    assertTrue( str, Email.isValidEmail(str) );

    str = "bjdhcudc@uoyuaclle.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "eaodljdyea829@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "fsckudckayuhdds@hdsuail.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ekaca66475@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "aqsaoisu_00@hdsuail.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "silly2242@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "Bdodmdva79@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ekaiual@aa.poiceasdc.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "saakay101@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ldouusce@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uyupasae@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "wkaoicu@assmldbal.com";
    assertTrue( str, Email.isValidEmail(str) );

    str = "obdslas@uuc.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uay1824@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "poasapuk@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "lkiczicm@lahuac.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "alacklick@edueaus.com";
    assertTrue( str, Email.isValidEmail(str) );

    str = "pkissy@ualdissa.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "klavacauu@poiceasdc.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "haobaos.klai@buu.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "eakddd@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "usuiaeoaw@jscd.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "mlacudc@poaueoipsivau.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "boasuki69@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "lauudu@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "lapdoafe@suucj.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "lufiuehassi@comueapa.com";
    assertTrue( str, Email.isValidEmail(str) );

    str = "jdhc_layuac@aaoshlick.com";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ulb955@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "updosudesdo1990@yahdd.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "wayca_lis99@hdsuail.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "jxl364@pus.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ldfb30@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ldmac@auail.ehdp.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "lpplycep@mw.cjup.dom";
    assertTrue( str, Email.isValidEmail(str) );

    str = "lydcumoaph@ucip.com";
    assertTrue( str, Email.isValidEmail(str) );

    str = "lydcumoaph@ucip.com";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ajl5@edocall.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uaviu_ua@ul.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "k8auak@hdsuail.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "boacuac.j.uaekay.1@cu.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "acuoawuahao14@hdsuail.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "auilyuahao@hdsuail.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "puahao@ealipaoedop.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "usuakaouki@hdua.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "juuaulayu@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uuaouy@oec.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uaok@suucj.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "boawjac@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uacial.uassicmly@yala.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uaaoph@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uhaokia747@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "luae321@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uusiekla19@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uemdvaoc@usaok-usaok.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "sauue@aeu.dom";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uaavauael3@hdsuail.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uuecaau@mibbdculaw.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "mdoojuuaui@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "acmalusmox@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ldoacuoieh@uicuupoicm.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "elasuia.uayao@su.abb.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ldoacuoieh@uicuupoicm.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "jeasd1857@comueapa.com";
    assertTrue( str, Email.isValidEmail(str) );

    str = "kuiuwddu@udlbid.poiceasdc.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "muillaos@ass.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "juiuehcao@puu.dom";
    assertTrue( str, Email.isValidEmail(str) );

    str = "vb4ucy1@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "auda314@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "jkdssuac@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "pzudusdllao@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "mbudscs@alsuci.poiceasdc.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "jkluusophy@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "cashacuc@alpha.lahuac.escy.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ucasmhsd@usa-aua.dom";
    assertTrue( str, Email.isValidEmail(str) );

    str = "jdac@poiceasdcaio.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "jcamlia@poiceasdc.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "aciubas@wvs.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "jaac@hauilsdcuspply.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "haccacdouqvius@hdsuail.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ludluac@udlbid.poiceasdc.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "williau_dluac@uaoek.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "acca8163@hdsuail.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "palsubdsacia@hdsuail.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "opacsial@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "hup3@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "pallup@aaoshlick.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "eap1112@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "upaooica@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "kipsiedc@yahdd.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "piaowdla@hdua.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "usoas8696@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "opdpial@aaoshcom.com";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uponetdciv@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "duup@uailaxeisa.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "kawlmal321@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "pauuyuqsicc@hdsuail.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "umodwao55@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "wacoah@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "viekiusa2@hdsuail.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "hoivacbsom@uaocdff.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uodbaosu@lawoaceavilla.dom";
    assertTrue( str, Email.isValidEmail(str) );

    str = "jodbicudc@hdua.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "odbicsd@aaoshlick.com";
    assertTrue( str, Email.isValidEmail(str) );

    str = "usauia@mws.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ubauaball2@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ehaeed309@hdua.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "acuky-3@axeisa.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "luacuall@udlbid.poiceasdc.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "luacuall@udlbid.poiceasdc.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "jbuehaafao@hdua.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "jauuuaa@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "usallaoiu@bimfdds.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "wuehiuual@waiuuuacuicsz.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uuehcakacbaom@udlbid.poiceasdc.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ovuehdcawalu@alsu.uis.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uedssy1157@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "buuhasmhc@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "lyccia6@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "zuu11@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uuhallaby@ass.com";
    assertTrue( str, Email.isValidEmail(str) );

    str = "auaoil500@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ldki029@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uhicuala@suucj.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uiedoa@aodlu.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "euiamlao@hdua.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uiuuu825@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "madukdvao@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "mukdvao@epesu.jcj.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "bassicaulaua@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "afaouha@yahdd.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "sahioih.uuish@ass.com";
    assertTrue( str, Email.isValidEmail(str) );

    str = "netao_uuish@aaoshlick.com";
    assertTrue( str, Email.isValidEmail(str) );

    str = "netao_uuish@aaoshlick.com";
    assertTrue( str, Email.isValidEmail(str) );

    str = "jaccyoabea@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uudoiaod@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "supalick@eu.poiceasdc.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "jupisz@pauuia.dom";
    assertTrue( str, Email.isValidEmail(str) );

    str = "auuyusaala@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "salfaio51@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "eoazybdea@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "husdca@oauaaoeh.cj.cae.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "juusdca@caoe.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "lusoma@ballaslacsie.com";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ausekawao@kcisa.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "pasapac3@yahdd.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "sasacdsi@dpsdclica.com";
    assertTrue( str, Email.isValidEmail(str) );

    str = "dlivao_b_sayldo@yahdd.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "s2c2kb@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "usappao@uacacj.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "jsdooau@poiceasdc.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "soauaica@aeu.dom";
    assertTrue( str, Email.isValidEmail(str) );

    str = "soauaica@ausod.poiceasdc.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "pldp321@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "esodpp50@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "laxusao48@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "vdalkaou@phaoua.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "laila@duciaoeh-cy.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uika@duciaoeh-cy.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uhw@eu.poiceasdc.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "kww118@pus.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "jdac_waocao@ul.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "eycshia.wnetboddk@ww-p.dom";
    assertTrue( str, Email.isValidEmail(str) );

    str = "cyaos10013@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ewillia@lawoaceavilla.dom";
    assertTrue( str, Email.isValidEmail(str) );

    str = "jsuy_williauu@puu.k12.cj.su";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ow@blauabdoc.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ehduakill@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ulwl@yahdd.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "vaod184@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uaomu22@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "Kasa.Woimhs@hdua.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "hsay775@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "slz13@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "usdffehaek@lyedu.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "acc@zslscao.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "kasa@zslscao.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "jsac.uauimsal@alsuci.uska.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "fkdc1@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "aebaoodwu@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "absuwim@hdsuail.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "kacmalua@lawoaceavilla.dom";
    assertTrue( str, Email.isValidEmail(str) );

    str = "epasaou72@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "siea_u@hdsuail.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "aiodpaqsa1@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "cauia@iau.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "kashoica@cy.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "fli@poiceasdc.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uue2002@edlsubia.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uvacoacu@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uvacoacu@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "eaousacuacfauily@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uayffaj@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "esolyaa@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uauaoshso@hdsuail.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "jueuaoudss33@edueaus.com";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uuiehal@assmldbal.com";
    assertTrue( str, Email.isValidEmail(str) );

    str = "jue415@pus.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uesbabdy14@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "juaoey_1999@yahdd.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uaekayao@be.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "se2013@baocaou.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "jacuaoudc@ehdasa.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "sacuaoudc@acudvao.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uikau@iueu.ossmaou.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "elaioab94@hdsuail.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "usacdu1133@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "laxksu@hdsuail.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "mddfy1629@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "Phlic32@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uusac63541@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ajaccicm@poiceasdc.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "kavic_ueicssoff@hdsuail.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "upassis@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "oiehaou@pdosheawl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "jiu@auave.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "kashybabedek@edueaus.com";
    assertTrue( str, Email.isValidEmail(str) );

    str = "u.baomuac@ass.com";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uaviubiuehdff@uuc.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "bliszfauily@uuc.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uaaoeh60@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "jauaujboaccac@uuc.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "jkju_eauboiuma@yahdd.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ajbsslao12@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ueldvao11@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "acshdcy.eapdc@faesiva.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uac_ehauby@ul.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "vaup767@hdsuail.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uiuimu@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "zsubsl@edueaus.com";
    assertTrue( str, Email.isValidEmail(str) );

    str = "kdzaoa@aaoshlick.com";
    assertTrue( str, Email.isValidEmail(str) );

    str = "lizusaey@uuc.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "swdaku@aaoshlick.com";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ulasss@pashuaok.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "laudc@ass.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "jjdyeaku@ass.com";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ueeaoshy4@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "jduhsa.uepasl@psuau.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "udcc4fiuh@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "accudcsdya@jscd.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uddoafauily12@edueaus.com";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ucaakyjaac@yahdd.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "sduusooay@edueaus.com";
    assertTrue( str, Email.isValidEmail(str) );

    str = "kally13c@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "maooy.dedccdo@haalshacuwaouice.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ysdac@balisdcd.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uzuszacca@yahdd.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "bdsazzic1@blddubaom.com";
    assertTrue( str, Email.isValidEmail(str) );

    str = "eicaua2102@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "epasaoudc@poiceasdc.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "hep275@yahdd.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "pylmoyu@ballaslacsie.com";
    assertTrue( str, Email.isValidEmail(str) );

    str = "aoaez@hdsuail.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "onetu@poiceasdc.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ehyoal87@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ooduaoiek@oec.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ssdpiacuoaau3flu@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "boass@ass.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "kuuish5678@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "aupoammu@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "kiskas51583@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uehswi@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "jfwalkao@ca2.su.ul.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ehoiuoclbi@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uwaaku@poiceasdc.net";
    assertTrue( str, Email.isValidEmail(str) );

    str = "jwaiuu@esh2a.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "pzacuuz@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "peypoauu@phaouavas.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "oduaf79e@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "sfilap@elaiuusaodclica.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uaoua.basuy.ia@ass.com";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uhaila_kaijska@hdsuail.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ukkosamao72@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "uuacuaou@pousu.jcj.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "upuoaau@adl.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "ukaulao@ualiubsoyuehddl.dom";
    assertTrue( str, Email.isValidEmail(str) );

    str = "miccauuish@yahdd.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "jauiaueac@hdsuail.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "jls565@yahdd.edu";
    assertTrue( str, Email.isValidEmail(str) );

    str = "paslbsmachamac@hdsuail.edu";
    assertTrue( str, Email.isValidEmail(str) );



  }

} // Test_Email

/*
 * $Log: Test_Email.java,v $
 * Revision 1.2  2003/02/26 03:38:47  rphall
 * Added copyright and GPL license
 *
 * Revision 1.1  2003/02/19 03:41:40  rphall
 * Working with unit tests
 *
 */

