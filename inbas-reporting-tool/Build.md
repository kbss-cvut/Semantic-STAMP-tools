# Building SISel

This document describes the build process of SISel.

## Reporting tool

#### Požadované prostředí

Pro sestavení produkčního archivu reportovacího nástroje (dále jen RT) je třeba mít nainstalované a správně nakonfigurované následující knihovny a platformy:

* JDK 8 (Ke stažení na [http://www.oracle.com/technetwork/java/javase/downloads/index-jsp-138363.html](http://www.oracle.com/technetwork/java/javase/downloads/index-jsp-138363.html)),
* Apache Maven 3.x (ke stažení  na [https://maven.apache.org/](https://maven.apache.org/)),
* NodeJS v6.x (Ke stažení na [https://nodejs.org/en/](https://nodejs.org/en/)),
* npm (Je součástí NodeJS distribuce).

Při sestavení je též nutný přístup k internetu.


#### Konfigurace

Před sestavením archivu lze měnit nastavení, které bude RT používat při běhu. Toto nastavení je rozděleno do několika
skupin, které mají samostatné konfigurační soubory.

##### Hlavní konfigurace RT

* Adresu hlavního úložiště, do kterého aplikace ukládá data - atribut `repositoryUrl`,
* Adresu úložiště, ve kterém se nachází taxonomie a slovníky používané RT - atribut `eventTypesRepository`,
* Adresu úložiště, které používá RT a generátor formulářů pro předávání dat, na jejichž základě se formuláře generují - atribut `formGenRepositoryUrl`,
* URL adresu webové služby generátoru formulářů - atribut `formGenServiceUrl`.

Všechna tato nastavení se nachází v souboru `src/main/resources/config.properties`.

Pokud při instalaci postupujete dle instalačního manuálu, není třeba tuto konfiguraci měnit, neboť je přednastavena pro hodnoty z manuálu.

##### Konfigurace analytického modulu

* Nastavení statistik pro hlavní stránku aplikace - atribut `statistics.dashboard`,
* Nastavení obecné statistiky - atribut `statistics.general`,
* Nastavení statistik dle typů událostí - atribut `statistics.eventType`,
* Nastavení statistik auditů - atribut `statistics.audit`,
* Nastavení statistik safety issues - atribut `statistics.safetyIssue`,
* Nastavení přehledu statistik pro Safety Action Group (SAG) - atribut `statistics.sag`.

Tato nastavení se nachází v souboru `src/main/resources/statistics.properties`.

Pokud při instalaci postupujete dle instalačního manuálu, není třeba tuto konfiguraci měnit, neboť je přednastavena pro hodnoty z manuálu.

##### Nastavení připojení na systém ECCAIRS

Systém SISEL získává ze systému ECCAIRS aktuální záznamy o událostech.
 
Pro správnou práci systému je třeba provést nakonfigurovat stávající instalaci systému ECCAIRS i systém SISEL.

###### Konfigurace systému ECCAIRS

ECCAIRS server poskutuje službu ECCAIRS Web API, kterou využívá systém SISEL pro komunikaci se systémem ECCAIRS. Tuto službu je nutné aktivovat v instalaci systému ECCAIRS dle instalační dokumentace systému ECCAIRS.

Po úspěšné aktivaci služby ECCAIRS Web API je tato služba dostupná na veřejné adrese "https://SERVER-NAME/eccairs5/webAPI.svc/basic-https", kde `SERVER-NAME`
označuje veřejnou adresu serveru na které je služba vystavena.

Pomocí aplikace ECCAIRS Browser se připojte k repozitáři, který chcete využít pro synchronizaci se systémem SISEL. 

###### Konfigurace systému SISEL

Nastavení připojení na systém ECCAIRS se nachází v souboru `src/main/resources/eccairs-config.properties`.

Toto nastavení zahrnuje:
* `eccairs.repository.id` - Identifikátor ECCAIRS repozitáře na vzdáleném serveru
* `eccairs.repository.language` - Jazyk ECCAIRS repozitáře
* `eccairs.repository.library` - Knihovna ECCAIRS dotazů, ze které budou využívány dotazy do systému ECCAIRS uvedené níže
* `eccairs.repository.query.getAllOccurrences` - Identifikátor dotazu v knihovně určené atributem `eccairs.repository.library`, který umožňuje získat z ECCAIRS serveru záznamy o všech událostech
* `eccairs.repository.query.getOccurrencesAfterCreatedDate` - Identifikátor dotazu v knihovně určené atributem `eccairs.repository.library`, který umožňuje získat z ECCAIRS serveru záznamy o událostech, které byly vytvořeny po zadaném datu.
* `eccairs.repository.query.getOccurrencesAfterModifiedDate` - Identifikátor dotazu v knihovně určené atributem `eccairs.repository.library`, který umožňuje získat z ECCAIRS serveru záznamy o událostech, které byly modifikovány po zadaném datu.
* `eccairs.repository.query.getOccurrenceByInitialFileNumberAndReportingEntity` - Identifikátor dotazu v knihovně určené atributem `eccairs.repository.library`, který umožňuje získat z ECCAIRS serveru záznam o události na základě identifikace této události u tvůrce záznamu, tedy na základě hodnoty atributu `file number` a `reporting entity`.
* `eccairs.repository.username` - uživatelské jméno pro přihlášení do systému ECCAIRS
* `eccairs.repository.password` - heslo pro přihlášení do systému ECCAIRS

##### Nastavení připojení na emailový server

Nastavení připojení na emailový server se nachází v souboru `src/main/resources/email-config.properties`. Používá se k připojení a monitorování emailové schránky.

Příklad obsahu konfiguračního souboru odpovídá standardním vlastnostem IMAP připojení. Příkladem je :

* `mail.store.protocol=imaps`
* `mail.imap.socketFactory.class=javax.net.ssl.SSLSocketFactory`
* `mail.imap.socketFactory.fallback`
* `mail.imap.socketFactory.port`
* `mail.user`
* `mail.password`
* `mail.server`
* `mail.folder`

##### Nastavení konfigurace pro transformaci ECCAIRS záznamů (E5X a E5F)

Pro nastavení konfigurace Konfigurace URL k serveru se nachází v souboru: 'src/main/resources/eccairs-e5xml-parser.properties'. Tento soubor obsahuje konfigurace, určené pro vývojáře systému, nikoliv pro běžnou obsluhu.
* `server` -  URL serveru, na kterém se nachází RDF repozitáře s ECCAIRS schematem
* `factory` - jméno Java třídy pro načítání ECCAIRS schematu
* `repoIdTemplate` - šablona pro verzované ID repozitáře s ECCAIRS schematem
* `eccairsContextTemplate` - šablona pro verzované ID grafu s ECCAIRS schematem


#### Sestavení

Po případných úpravách konfigurace lze přistoupit k sestavení produkční verze systému.

1. Je třeba nastavit cestu do hlavní složky RT.
2. Spustit sestavení příkazem `mvn clean package -p production`.
    1. Tento krok může trvat několik minut.
4. Výsledný archiv nese název `sisel.war` a nachází se ve složce `target`.

Archiv `sisel.war` lze nakopírovat do aplikačního serveru, kde bude pak aplikace dostupná na adrese `http://adresa.serveru/sisel`, 
kde `http://adresa.serveru/` je adresa, na které je aplikační server dostupný.