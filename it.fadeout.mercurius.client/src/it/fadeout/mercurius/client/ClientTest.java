package it.fadeout.mercurius.client;

import it.fadeout.mercurius.business.Contact;
import it.fadeout.mercurius.business.Group;

import java.util.List;

public class ClientTest {
  public static void main(String[] args) {
    
	  //MercuriusAPI oAPI = new MercuriusAPI("http://localhost:8080/it.fadeout.mercurius.webapi");
	  MercuriusAPI oAPI = new MercuriusAPI("http://130.251.104.84:8080/it.fadeout.mercurius.webapi");
	  
//	  System.setProperty ("http.proxyHost", "127.0.0.1");
//	  System.setProperty ("http.proxyPort", "8889");
	    
	  ClientTest oTest = new ClientTest();
	  //oTest.TestContacts(oAPI);
	  //oTest.TestSms(oAPI);
	  oTest.TestMails(oAPI);
  }

  public void TestContacts(MercuriusAPI oAPI) {
	  
	  Contact oInsertContact = new Contact();
	  oInsertContact.setName("Pinco");
	  oInsertContact.setSurname("Pallino");
	  int iIDContact = oAPI.createContact(oInsertContact);
	  
	  Contact oContact = oAPI.getContact(iIDContact);
	  
	  if (oContact != null) {
		  System.out.println(oContact.getName() + " " + oContact.getSurname());
	  }
	  
	  List<Contact> aoContacts = oAPI.getAllContacts();
	  if (aoContacts!= null) {
		  System.out.println("Contacts = " + aoContacts.size());
	  }
	  
	  oContact.setEMail("pinco@pallino.it");
	  
	  oAPI.updateContact(oContact);
	  //oAPI.deleteContact(oContact.getIdContact());
	  
  }
  
  public void TestSms(MercuriusAPI oAPI) {
	  String sContacts = "22;24";
	  oAPI.sendSmsToContacts(sContacts, "Ora inviamo agli Id dei Contatti");

	  List<Group> aoGroups = oAPI.getAllGroups();
	  if (aoGroups!=null) {
		  if (aoGroups.size()>0) {
			  oAPI.sendSmsToGroup(aoGroups.get(0).getIdGroup(), "Ciao ciao da Paolo");
		  }
	  }
	  
	  String sAddresses = "+393493506892;3396713586";
	  oAPI.sendSmsDirect(sAddresses, "Questo invece e' un invio diretto");	  
  }
  
  public void TestMails(MercuriusAPI oAPI) {
	  /*
	  String sContacts = "22;24";
	  //oAPI.sendMailToContacts(sContacts, "Ora inviamo agli Id dei Contatti");
	  
	  List<Group> aoGroups = oAPI.getAllGroups();
	  if (aoGroups!=null) {
		  if (aoGroups.size()>0) {
			  oAPI.sendMailToGroup(aoGroups.get(0).getIdGroup(), "Ciao ciao da Paolo");
		  }
	  }
	  */
	  
	  String sAddresses = "p.campanella@fadeout.it;alecottino@acrotec.it";
	  
	  //String sAddresses = "anna.adinolfi@gmail.com;serenellafrancesco@alice.it;delfrancis@libero.it;stefan_6@libero.it;catianse@vodafone.it;mondonaturaarenzano@gmail.com;mondonaturaarenzano@gmail.com;arenzanomarmi@libero.it;areccomonica3@gmail.com;lauraarecco@gmail.com;marialaura.asunis@poste.it;nanni59@yahoo.it;lella.barbotto@gmail.com;adry.baretella@libero.it;angelarossobarone@libero.it;rbaudizzone@comped.it;christine.beccaro@gmail.com;f.benevelli@alice.it;info@ristoranteterraefuoco.com;simonabergaglio@yahoo.it;cbergero@gmail.com;monicabiagi@vodafone.it;monicabiagi@vodafone.it;federicobianchi3@virgilio.it;giorgioecristina@libero.it;mlbiorci@hotmail.com;oggesimona@hotmail.com;bbonadei@gmail.com;cribongio@gmail.com;barbara.8bonzo@gmail.com;b_borgianini@hotmail.it;arenzano@jaggywear.it;info@traversocadeaux.it;tennisclubarenzano@gmail.com;annalisabottino@gmail.com;franda61@alice.it;rosangelabozzano@hotmail.it;brero@fastwebnet.it;roberto.brero@me.com;lenabria@yahoo.it;avvbriasco@gmail.com;brioskj@gmail.com;ristorante@tanin.it;mark-anty@libero.it;alberto.bruzzone@beonesolutions.com;esther.buccino@gmail.com;caffe71@gmail.com;info@albatros-hotel.it;barbara.calcagno@alice.it;manu.calcagno89@gmail.com;giovanni1893@hotmail.com;lubiacalcagno@gmail.com;calcagno.luca@gmail.com;maricalc@gmail.com;renata.calcagno@libero.it;rpc3068@gmail.com;simo.calcagno@virgilio.it;m.calcagno1@virgilio.it;canale.lorenza@libero.it;canepa.giovanni@tin.it;cipi61.cc@libero.it;girovagando@gmail.com;eleonora.cantu@evolutiontravel.it;mauro.vinnatur@gmail.com;fra-capo@live.it;ilaria.cappelletti@hotmail.it;debora@studiobadaracco.com;debora@studiobadaracco.com;info@vicosouvenir.it;silvy1606@hotmail.com;alessandro.casale72@gmail.com;claudiacassinelli9@gmail.com;silvia.cavaletti@gmail.com;cinzia.cav@mclink.it;pleasearenzano@gmail.com;larala@inwind.it;caviglia_franco@fastwebnet.it;antonelcav@libero.it;caterinalibri.caterinali@tin.it;cristina.caviglia77@gmail.com;maura.caviglione@gmail.com;luqa.cenci@gmail.com;maria.cerminara.90@gmail.com;rosina.cerra@comune.arenzano.ge.it;fabio.cherici89@gmail.com;alicechiefalo@hotmail.it;giadachiefalo@gmail.com;fedeciba@libero.it;chiara.cisamolo@me.com;graziellaclemente2@gmail.com;giovannaclivio@gmail.com;giorgio.colace@fastwebnet.it;corradi.sara@gmail.com;andreacoscia@libero.it;zietta_88@libero.it;mcatardi@gmail.com;augusto.costa57@alice.it;alecottino@acrotec.it;barbaracrippa74@gmail.com;sonia.cubeddu@yahoo.it;francesca.cuttica@gmail.com;dambrosiogiusi74@gmail.com;damonte.a@gmail.com;emanuela.damonte@libero.it;gianluca.damonte5@alice.it;valda91@alice.it;edoabria@libero.it;gerry.damonte@gmail.com;gmdamonte@libero.it;gbdamonte@aol.it;damontsina@hotmail.it;ilaria.damonte@gmail.com;info@immobiliaredamonte.com;info@immobiliaredamonte.com;mauro.damonte@alice.it;daniele.debernardi@gmail.com;biby81@libero.it;degradoandrea@gmail.com;linton.r@alice.it;kiarettaxox83@gmail.com;fede.delfino78@gmail.com;fradolphin@gmail.com;maria-grazia.delfino@riccaf.it;silvanadelfino@gmail.com;stefaniadelfino73@gmail.com;tizydelfino@libero.it;angelo.calcagno@fastwebnet.it;dellerbar@hotmail.com;davidedeluca967@gmail.com;gabriele.desidera@gmail.com;gianna.tonino@alice.it;elisabettadroguet@hotmail.com;villavenetosrl@libero.it;anna.arena49@gmail.com;info@3stylerstore.it;andreaorkidea@yahoo.it;annaclelia_ferrando@libero.it;isa.mammabonta@gmail.com;pinoferr64@alice.it;luigiebenedetta@me.com;cristina.fiocchi@h3g.it;easybaby@email.it;direzione@pec.muvita.it;paolo.fonsetti@libero.it;cristina.fornitocchi@gmail.com;franchiniwalter@me.com;streppi@hotmail.it;erja84@hotmail.it;caporicevimento@gharenzano.it;simone.gabellani@cimafoundation.org;info@gaggero.it;max_titti@libero.it;sgalanti80@gmail.com;nuvolina9086@hotmail.it;gallettino72@gmail.com;erikagambino@libero.it;monica.gasperi@gmail.com;paolagatti79@yahoo.it;simone.gavarone@comune.arenzano.ge.it;klagiacchi@gmail.com;soniagiancontieri@hotmail.it;dcollu@gmail.com;giampiero.giglio@comune.arenzano.ge.it;fabio.cherici89@gmail.com;corinnagiussani@gmail.com;ilprato.lerca@gmail.com;giusi.giusti@email.it;angelogiusto85@gmail.com;glendamarmondi@gmail.com;mgrande.cell@gmail.com;grandi.claudia@alice.it;roberta.grassi@villaggio.org;a.grassi48@gmail.com;aldo.greco@tin.it;anna.oli51@virgilio.it;patrizia.grillo@alice.it;seregro@libero.it;info@danycuir.it;rgrume@tin.it;info@poggiohotel.it;info@albatros-hotel.it;carloiaccheri@brt.it;fcanuso@hotmail.it;rosario.ioele@teletu.it;sere.ali.ale@gmail.com;paolo1457@gmail.com;paolo1457@gmail.com;ivaldi.fabio@gmail.com;lamberi.luciano@gmail.com;gplbach@gmail.com;carlolevo@alice.it;attilio.lieto@gmail.com;serena@saldotecnicaligure.com;alessandra@biofarms.net;laura.star86@hotmail.it;nico.maenza@alice.it;robimagio@yahoo.it;valemala2000@yahoo.it;dadalella@alice.it;malaspina.franco@gmail.com;sabrina.mallia@allice.it;mandictatjans@yahoo.it;antonella.marasco@gmail.com;ros.marchesi@gmail.com;marco.marchiol@bancapatrimoni.it;mcatardi@gmail.com;andreamaricone@libero.it;icigirl79@gmail.com;baccino.matteo@gmail.com;gabriella.mellogno@gmail.com;annamerello@alice.it;iltorchioarenzano@gmail.com;giovannimicera@gmail.com;michelinir@aen.ansaldo.it;nazario.minetti@katamail.com;cecilia.misurale@gmail.com;niki.moggia@fastwebnet.it;paolo@speedvet.it;edoardo.mongiardini@ericsson.com;framor@fastwebnet.it;PAOLAMOSCA79@LIBERO.IT;info@comunionepinetadiarenzano.it;mustodaniela@hotmail.it;luc.sar@tiscalinet.it;salvatore.nastro79@gmail.com;emanuelanerboni@gmail.com;giorgionoli@gmail.com;sara9@libero.it;marco.orsatti@berner.it;ottonello.b@gmail.com;pimpfloyd@rocketmail.com;lutalaba@libero.it;bettapala@gmail.com;marianna.palermo@virgilio.it;andrea.parodi75@libero.it;silvi.p84@live.it;angeloorlando@studioprofim.it;silvi.p84@live.it;parodi.cris@gmail.com;lapo.pasqui@gmail.com;022.genova@ageallianz.it;marinafotoarenzano@libero.it;andrep3@hotmail.it;carlocipollini@alice.it;pellegrino.arenzano@email.it;cristinapelosi@hotmail.it;spydermandf@gmail.com;gabriella.perra@fastwebnet.it;marcopesce82@yahoo.it;carop1991@hotmail.it;luigipiccardo@gmail.com;sichele09@gmail.com;quintina_piono@fastwebnet.it;fabio.piscolo@bancopopolare.it;ale.p1970@libero.it;luisa.polito@tiscali.it;leforme@libero.it;pontise@alice.it;am.pozzoli@gmail.com;procopioviviana@yahoo.it;puddufra@gmail.com;illalla2002@gmail.com;laurarecagno@gmail.com;23tatanga26@gmail.com;ema65@live.it;riccardo.ridolfi@alice.it;roccoirene@live.it;sabrinarivanegra@yahoo.it;oreste.rizzello@libero.it;giusepperoba@gmail.com;paola_ro@libero.it;marina.robello@gmail.com;m.roetto@softjam.it;fronzit@tin.it;calcross@tin.it;lucamichi@live.it;russo.francesco@outlook.com;sabafranci@libero.it;salsi_emanuela@yahoo.it;alessandra.salvatico@gmail.com;cinghialeubriaco@gmail.com;fina75@libero.it;fina75@libero.it;mauriziosantin.ms@libero.it;luigisartore007@gmail.com;marina.scabini@gmail.com;ritascappaticci@gmail.com;nanniscaraffia@gmail.com;adriano.servetto@alice.it;patriciashea2003@yahoo.com;sifonetti.antonio@libero.it;sifonetti.antonio@libero.it;soffiotto@libero.it;sammaquattro@libero.it;susy.orsi@tiscali.it;massimo.sqm@alice.it;uffi.iris@gnail.com;g.peola@libero.it;o.tabor@libero.it;monica.tacchino@libero.it;rarinantesrex@gmail.com;talaricoottavio@libero.it;daniela.tao@virgilio.it;rossellatarantino86@gmail.com;pstarikka@yahoo.it;giuliano.tasso.75@gmail.com;terenzi@studiolegalefts.it;stefania.terrinoni@alice.it;a.tessore@acrotec.it;Alessandro.Tomaselli@regione.liguria.it;p.patriziatoro@gmail.com;giuse.torre@gmail.com;jomorens@gmail.com;bibbare@me.com;paola.costatoso@gmail.com;ginkog@gmail.com;paola.costatoso@gmail.com;paola.costatoso@gmail.com;aurelioginevra@gmail.com;carlobruna09@libero.it;gioturbix@gmail.com;ced@comune.arenzano.ge.it;ced@comune.arenzano.ge.it;ced@comune.arenzano.ge.it;agrivallarino@gmail.com;giovanna.vallarino@gmail.com;lazzarovall@yahoo.it;valle.cristina@fastwebnet.it;giusi.valle@libero.it;capopan@gmail.com;varone.giovanniemilio@gdf.it;mickell@libero.it;beselettrico@gmail.com;cosimo@acrotec.it;vigofrancesco@hotmail.it;carolavigo@yahoo.it;";
	  
	  //String sAddresses1 = "anna.adinolfi@gmail.com;serenellafrancesco@alice.it;delfrancis@libero.it;stefan_6@libero.it;catianse@vodafone.it;mondonaturaarenzano@gmail.com;mondonaturaarenzano@gmail.com;arenzanomarmi@libero.it;areccomonica3@gmail.com;lauraarecco@gmail.com;marialaura.asunis@poste.it;nanni59@yahoo.it;lella.barbotto@gmail.com;adry.baretella@libero.it;angelarossobarone@libero.it;rbaudizzone@comped.it;christine.beccaro@gmail.com;f.benevelli@alice.it;info@ristoranteterraefuoco.com;simonabergaglio@yahoo.it;cbergero@gmail.com;monicabiagi@vodafone.it;monicabiagi@vodafone.it;federicobianchi3@virgilio.it;giorgioecristina@libero.it;mlbiorci@hotmail.com;oggesimona@hotmail.com;bbonadei@gmail.com;cribongio@gmail.com;barbara.8bonzo@gmail.com;b_borgianini@hotmail.it;arenzano@jaggywear.it;info@traversocadeaux.it;tennisclubarenzano@gmail.com;annalisabottino@gmail.com;franda61@alice.it;rosangelabozzano@hotmail.it;brero@fastwebnet.it;roberto.brero@me.com;lenabria@yahoo.it;avvbriasco@gmail.com;brioskj@gmail.com;ristorante@tanin.it;mark-anty@libero.it;alberto.bruzzone@beonesolutions.com;esther.buccino@gmail.com;caffe71@gmail.com;info@albatros-hotel.it;barbara.calcagno@alice.it;manu.calcagno89@gmail.com;giovanni1893@hotmail.com;lubiacalcagno@gmail.com;calcagno.luca@gmail.com;maricalc@gmail.com;renata.calcagno@libero.it;rpc3068@gmail.com;";
	  //String sAddresses2 = "simo.calcagno@virgilio.it;m.calcagno1@virgilio.it;canale.lorenza@libero.it;canepa.giovanni@tin.it;cipi61.cc@libero.it;girovagando@gmail.com;eleonora.cantu@evolutiontravel.it;mauro.vinnatur@gmail.com;fra-capo@live.it;ilaria.cappelletti@hotmail.it;debora@studiobadaracco.com;debora@studiobadaracco.com;info@vicosouvenir.it;silvy1606@hotmail.com;alessandro.casale72@gmail.com;claudiacassinelli9@gmail.com;silvia.cavaletti@gmail.com;cinzia.cav@mclink.it;pleasearenzano@gmail.com;larala@inwind.it;caviglia_franco@fastwebnet.it;antonelcav@libero.it;caterinalibri.caterinali@tin.it;cristina.caviglia77@gmail.com;maura.caviglione@gmail.com;luqa.cenci@gmail.com;maria.cerminara.90@gmail.com;rosina.cerra@comune.arenzano.ge.it;fabio.cherici89@gmail.com;alicechiefalo@hotmail.it;giadachiefalo@gmail.com;fedeciba@libero.it;chiara.cisamolo@me.com;graziellaclemente2@gmail.com;giovannaclivio@gmail.com;giorgio.colace@fastwebnet.it;corradi.sara@gmail.com;andreacoscia@libero.it;zietta_88@libero.it;mcatardi@gmail.com;augusto.costa57@alice.it;alecottino@acrotec.it;barbaracrippa74@gmail.com;sonia.cubeddu@yahoo.it;";
	  //String sAddresses3 = "francesca.cuttica@gmail.com;dambrosiogiusi74@gmail.com;damonte.a@gmail.com;emanuela.damonte@libero.it;gianluca.damonte5@alice.it;valda91@alice.it;edoabria@libero.it;gerry.damonte@gmail.com;gmdamonte@libero.it;gbdamonte@aol.it;damontsina@hotmail.it;ilaria.damonte@gmail.com;info@immobiliaredamonte.com;info@immobiliaredamonte.com;mauro.damonte@alice.it;daniele.debernardi@gmail.com;biby81@libero.it;degradoandrea@gmail.com;linton.r@alice.it;kiarettaxox83@gmail.com;fede.delfino78@gmail.com;fradolphin@gmail.com;maria-grazia.delfino@riccaf.it;silvanadelfino@gmail.com;stefaniadelfino73@gmail.com;tizydelfino@libero.it;angelo.calcagno@fastwebnet.it;dellerbar@hotmail.com;davidedeluca967@gmail.com;gabriele.desidera@gmail.com;gianna.tonino@alice.it;elisabettadroguet@hotmail.com;villavenetosrl@libero.it;anna.arena49@gmail.com;info@3stylerstore.it;andreaorkidea@yahoo.it;annaclelia_ferrando@libero.it;";
	  //String sAddresses4 = "isa.mammabonta@gmail.com;pinoferr64@alice.it;luigiebenedetta@me.com;cristina.fiocchi@h3g.it;easybaby@email.it;direzione@pec.muvita.it;paolo.fonsetti@libero.it;cristina.fornitocchi@gmail.com;franchiniwalter@me.com;streppi@hotmail.it;erja84@hotmail.it;caporicevimento@gharenzano.it;simone.gabellani@cimafoundation.org;info@gaggero.it;max_titti@libero.it;sgalanti80@gmail.com;nuvolina9086@hotmail.it;gallettino72@gmail.com;erikagambino@libero.it;monica.gasperi@gmail.com;paolagatti79@yahoo.it;simone.gavarone@comune.arenzano.ge.it;klagiacchi@gmail.com;soniagiancontieri@hotmail.it;dcollu@gmail.com;giampiero.giglio@comune.arenzano.ge.it;fabio.cherici89@gmail.com;corinnagiussani@gmail.com;ilprato.lerca@gmail.com;giusi.giusti@email.it;angelogiusto85@gmail.com;glendamarmondi@gmail.com;mgrande.cell@gmail.com;grandi.claudia@alice.it;roberta.grassi@villaggio.org;a.grassi48@gmail.com;";
	  //String sAddresses5 = "aldo.greco@tin.it;anna.oli51@virgilio.it;patrizia.grillo@alice.it;seregro@libero.it;info@danycuir.it;rgrume@tin.it;info@poggiohotel.it;info@albatros-hotel.it;carloiaccheri@brt.it;fcanuso@hotmail.it;rosario.ioele@teletu.it;sere.ali.ale@gmail.com;paolo1457@gmail.com;paolo1457@gmail.com;ivaldi.fabio@gmail.com;lamberi.luciano@gmail.com;gplbach@gmail.com;carlolevo@alice.it;attilio.lieto@gmail.com;serena@saldotecnicaligure.com;alessandra@biofarms.net;laura.star86@hotmail.it;nico.maenza@alice.it;robimagio@yahoo.it;valemala2000@yahoo.it;dadalella@alice.it;malaspina.franco@gmail.com;sabrina.mallia@allice.it;mandictatjans@yahoo.it;antonella.marasco@gmail.com;ros.marchesi@gmail.com;marco.marchiol@bancapatrimoni.it;mcatardi@gmail.com;andreamaricone@libero.it;icigirl79@gmail.com;baccino.matteo@gmail.com;gabriella.mellogno@gmail.com;annamerello@alice.it;iltorchioarenzano@gmail.com;giovannimicera@gmail.com;michelinir@aen.ansaldo.it;nazario.minetti@katamail.com;cecilia.misurale@gmail.com;niki.moggia@fastwebnet.it;paolo@speedvet.it;edoardo.mongiardini@ericsson.com;framor@fastwebnet.it;PAOLAMOSCA79@LIBERO.IT;";
	  //String sAddresses6 = "info@comunionepinetadiarenzano.it;mustodaniela@hotmail.it;luc.sar@tiscalinet.it;salvatore.nastro79@gmail.com;emanuelanerboni@gmail.com;giorgionoli@gmail.com;sara9@libero.it;marco.orsatti@berner.it;ottonello.b@gmail.com;pimpfloyd@rocketmail.com;lutalaba@libero.it;bettapala@gmail.com;marianna.palermo@virgilio.it;andrea.parodi75@libero.it;silvi.p84@live.it;angeloorlando@studioprofim.it;silvi.p84@live.it;parodi.cris@gmail.com;lapo.pasqui@gmail.com;022.genova@ageallianz.it;marinafotoarenzano@libero.it;andrep3@hotmail.it;carlocipollini@alice.it;pellegrino.arenzano@email.it;cristinapelosi@hotmail.it;spydermandf@gmail.com;gabriella.perra@fastwebnet.it;marcopesce82@yahoo.it;carop1991@hotmail.it;luigipiccardo@gmail.com;sichele09@gmail.com;quintina_piono@fastwebnet.it;fabio.piscolo@bancopopolare.it;ale.p1970@libero.it;luisa.polito@tiscali.it;leforme@libero.it;pontise@alice.it;am.pozzoli@gmail.com;";
	  //String sAddresses7 = "procopioviviana@yahoo.it;puddufra@gmail.com;illalla2002@gmail.com;laurarecagno@gmail.com;23tatanga26@gmail.com;ema65@live.it;riccardo.ridolfi@alice.it;roccoirene@live.it;sabrinarivanegra@yahoo.it;oreste.rizzello@libero.it;giusepperoba@gmail.com;paola_ro@libero.it;marina.robello@gmail.com;m.roetto@softjam.it;fronzit@tin.it;calcross@tin.it;lucamichi@live.it;russo.francesco@outlook.com;sabafranci@libero.it;salsi_emanuela@yahoo.it;alessandra.salvatico@gmail.com;cinghialeubriaco@gmail.com;fina75@libero.it;fina75@libero.it;mauriziosantin.ms@libero.it;luigisartore007@gmail.com;marina.scabini@gmail.com;ritascappaticci@gmail.com;nanniscaraffia@gmail.com;adriano.servetto@alice.it;patriciashea2003@yahoo.com;sifonetti.antonio@libero.it;sifonetti.antonio@libero.it;soffiotto@libero.it;sammaquattro@libero.it;susy.orsi@tiscali.it;massimo.sqm@alice.it;uffi.iris@gnail.com;g.peola@libero.it;";
	  //String sAddresses8 = "o.tabor@libero.it;monica.tacchino@libero.it;rarinantesrex@gmail.com;talaricoottavio@libero.it;daniela.tao@virgilio.it;rossellatarantino86@gmail.com;pstarikka@yahoo.it;giuliano.tasso.75@gmail.com;terenzi@studiolegalefts.it;stefania.terrinoni@alice.it;a.tessore@acrotec.it;Alessandro.Tomaselli@regione.liguria.it;p.patriziatoro@gmail.com;giuse.torre@gmail.com;jomorens@gmail.com;bibbare@me.com;paola.costatoso@gmail.com;ginkog@gmail.com;paola.costatoso@gmail.com;paola.costatoso@gmail.com;aurelioginevra@gmail.com;carlobruna09@libero.it;gioturbix@gmail.com;ced@comune.arenzano.ge.it;ced@comune.arenzano.ge.it;ced@comune.arenzano.ge.it;agrivallarino@gmail.com;giovanna.vallarino@gmail.com;lazzarovall@yahoo.it;valle.cristina@fastwebnet.it;giusi.valle@libero.it;capopan@gmail.com;varone.giovanniemilio@gdf.it;mickell@libero.it;beselettrico@gmail.com;cosimo@acrotec.it;vigofrancesco@hotmail.it;carolavigo@yahoo.it;";
	  
	  
	  
	  //String sMessage = "Questo invece e' un invio diretto";
	  String sMessage = "Allerta arancione per neve dalle 18 di oggi a stanotte alle 3. Scuole e impianti sportivi aperti.";
	  String sTitle = "Comune di Arenzano - Avviso";
	  
	  //oAPI.sendMailDirect(sAddresses, sMessage);
	  oAPI.sendMailDirect(sAddresses, "Questo invece e' un invio diretto","Guarda chi invia", "misterx@mister.com");
	  //oAPI.sendMailDirect(sAddresses, sMessage, sTitle, "prevenzionecomune@cimafoundation.org");
	  /*
	  oAPI.sendMailDirect(sAddresses1, sMessage, sTitle, "prevenzionecomune@cimafoundation.org");
	  oAPI.sendMailDirect(sAddresses2, sMessage, sTitle, "prevenzionecomune@cimafoundation.org");
	  oAPI.sendMailDirect(sAddresses3, sMessage, sTitle, "prevenzionecomune@cimafoundation.org");
	  oAPI.sendMailDirect(sAddresses4, sMessage, sTitle, "prevenzionecomune@cimafoundation.org");
	  oAPI.sendMailDirect(sAddresses5, sMessage, sTitle, "prevenzionecomune@cimafoundation.org");
	  oAPI.sendMailDirect(sAddresses6, sMessage, sTitle, "prevenzionecomune@cimafoundation.org");
	  oAPI.sendMailDirect(sAddresses7, sMessage, sTitle, "prevenzionecomune@cimafoundation.org");
	  oAPI.sendMailDirect(sAddresses8, sMessage, sTitle, "prevenzionecomune@cimafoundation.org");
	  */
  }  
} 