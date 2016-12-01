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

##### Konfigurace analytického modulu

* Nastavení statistik pro hlavní stránku aplikace - atribut `statistics.dashboard`,
* Nastavení obecné statistiky - atribut `statistics.general`,
* Nastavení statistik dle typů událostí - atribut `statistics.eventType`,
* Nastavení statistik auditů - atribut `statistics.audit`,
* Nastavení statistik safety issues - atribut `statistics.safetyIssue`.

Tato nastavení se nachází v souboru `src/main/resources/statistics.properties`.

Byl-li pro instalaci a spuštění ostatních služeb použit návod dodávaný spolu s aplikací, není třeba konfiguraci RT
měnit, neboť je přednastavena pro hodnoty z návodu.

__//TODO Ještě eccairs a email konfigurace__

#### Sestavení

Po případných úpravách konfigurace lze přistoupit k sestavení produkční verze systému.

1. Je třeba nastavit cestu do hlavní složky RT.
2. Spustit sestavení příkazem `mvn clean package -p production`.
    1. Tento krok může trvat několik minut.
4. Výsledný archiv nese název `sisel.war` a nachází se ve složce `target`.

Archiv `sisel.war` lze nakopírovat do aplikačního serveru, kde bude pak aplikace dostupná na adrese `http://adresa.serveru/sisel`, 
kde `http://adresa.serveru/` je adresa, na které je aplikační server dostupný.