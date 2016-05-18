/**
 * Czech localization.
 */

var Constants = require('../constants/Constants');

module.exports = {
    'locale': 'cs',

    'messages': {
        'add': 'Přidat',
        'back': 'Zpět',
        'cancel': 'Zrušit',
        'close': 'Zavřít',
        'cancel-tooltip': 'Zrušit a zahodit změny',
        'save': 'Uložit',
        'delete': 'Smazat',
        'headline': 'Název',
        'narrative': 'Popis',
        'fileNo': 'Číslo záznamu',
        'table-actions': 'Akce',
        'table-edit': 'Editovat',
        'save-success-message': 'Hlášení úspěšně uloženo.',
        'save-failed-message': 'Hlášení nelze uložit. Odpověď serveru: ',
        'author': 'Autor',
        'author-title': 'Autor hlášení',
        'description': 'Popis',
        'select.default': '--- Vybrat ---',
        'yes': 'Ano',
        'no': 'Ne',
        'unknown': 'Neznámé',

        'detail.save-tooltip': 'Uložit změny',
        'detail.saving': 'Ukládám...',
        'detail.invalid-tooltip': 'Některá povinná pole nejsou vyplněna',
        'detail.submit': 'Odeslat do SAG',
        'detail.submit-tooltip': 'Odeslat současnou revizi zprávy do SAG a vytvořit novou revizi',
        'detail.large-time-diff-tooltip': 'Časový rozdíl počátku a konce události je příliš velký',
        'detail.submit-success-message': 'Zpráva úspěšně odeslána.',
        'detail.submit-failed-message': 'Hlášení se nepodařilo odeslat. Odpověď serveru: ',
        'detail.loading': 'Načítám hlášení...',
        'detail.not-found.title': 'Hlášení nenalezeno',


        'login.title': Constants.APP_NAME + ' - Přihlášení',
        'login.username': 'Uživatelské jméno',
        'login.password': 'Heslo',
        'login.submit': 'Přihlásit',
        'login.register': 'Registrace',
        'login.error': 'Přihlášení se nezdařilo.',
        'login.progress-mask': 'Přihlašuji...',

        'register.title': Constants.APP_NAME + ' - Nový uživatel',
        'register.first-name': 'Jméno',
        'register.last-name': 'Příjmení',
        'register.username': 'Uživatelské jméno',
        'register.password': 'Heslo',
        'register.password-confirm': 'Potvrzení hesla',
        'register.passwords-not-matching-tooltip': 'Heslo a jeho potvrzení se neshodují',
        'register.submit': 'Registrovat',
        'register.mask': 'Registruji...',

        'main.dashboard-nav': 'Hlavní strana',
        'main.reports-nav': 'Hlášení',
        'main.statistics-nav': 'Statistiky',
        'main.logout': 'Odhlásit se',

        'dashboard.welcome': 'Dobrý den, {name}, vítejte v ' + Constants.APP_NAME + '.',
        'dashboard.create-tile': 'Vytvořit hlášení o události',
        'dashboard.search-tile': 'Hledat hlášení',
        'dashboard.search-placeholder': 'Název hlášení',
        'dashboard.view-all-tile': 'Prohlížet všechna hlášení',
        'dashboard.create-empty-tile': 'Začít s prázdným hlášením',
        'dashboard.create-import-tile': 'Importovat prvotní hlášení',
        'dashboard.recent-panel-heading': 'Nedávno přidaná/upravená hlášení',
        'dashboard.recent-table-headline': 'Název hlášení',
        'dashboard.recent-table-date': 'Datum události',
        'dashboard.recent-table-last-edited': 'Naposledy upraveno',

        'reports.no-occurrence-reports': 'Zatím nebylo vytvořeno žádné hlášení.',
        'reports.no-reports': 'Nenalezena žádná hlášení. Nové hlášení můžete vytvořit ',
        'reports.no-reports.link': 'zde.',
        'reports.no-reports.link-tooltip': 'Jít na hlavní stránku',
        'reports.open-tooltip': 'Kliknutím zobrazíte detail hlášení',
        'reports.edit-tooltip': 'Editovat toto hlášení',
        'reports.delete-tooltip': 'Smazat toto hlášení',
        'reports.loading-mask': 'Nahrávám hlášení...',
        'reports.panel-title': 'Hlášení událostí',
        'reports.table-date': 'Datum a čas události',
        'reports.table-occurrence-category': 'Kategorie události',
        'reports.table-type': 'Typ',
        'reports.table-classification': 'Kategorie',
        'reports.table-classification.tooltip': 'Vyberte kategorií událostí, kterou chcete zobrazit',
        'reports.filter.label': 'Zobrazit',
        'reports.filter.type.tooltip': 'Vyberte typ hlášení, která chcete zobrazit',
        'reports.filter.type.all': 'Všechna',
        'reports.filter.type.preliminary': 'Předběžná',
        'reports.filter.no-matching-found': 'Žádná hlášení neodpovídají zvoleným parametrům.',
        'reports.filter.reset': 'Zrušit filtry',
        'reports.create-report': 'Nové hlášení',

        'delete-dialog.title': 'Smazat hlášení?',
        'delete-dialog.content': 'Skutečně chcete smazat toto hlášení?',
        'delete-dialog.submit': 'Smazat',

        'occurrence.headline-tooltip': 'Krátké pojmenování události - pole je povinné',
        'occurrence.start-time': 'Počátek události',
        'occurrence.start-time-tooltip': 'Datum a čas kdy k události došlo',
        'occurrence.end-time': 'Konec události',
        'occurrence.end-time-tooltip': 'Datum a čas kdy událost skončila',
        'occurrence.class': 'Třída závažnosti',
        'occurrence.class-tooltip': 'Třída závažnosti - pole je povinné',

        'initial.panel-title': 'Prvotní hlášení',
        'initial.table-report': 'Hlášení',
        'initial.wizard.add-title': 'Přidat prvotní hlášení',
        'initial.wizard.edit-title': 'Editovat prvotní hlášení',
        'initial.label': 'Prvotní hlášení',
        'initial.tooltip': 'Text prvotního hlášení - pole je povinné',

        'preliminary.type': 'Předběžné',
        'preliminary.new': 'Nové hlášení',
        'preliminary.table-investigate': 'Šetřit',
        'preliminary.table-investigate-tooltip': 'Zahájit šetření události',
        'preliminary.detail.panel-title': 'Předběžné hlášení o události',
        'preliminary.detail.last-edited-msg': 'Naposledy upravil(a) {name} {date}.',
        'preliminary.detail.narrative-tooltip': 'Popis - pole je povinné',
        'preliminary.detail.table-edit-tooltip': 'Editovat položku',
        'preliminary.detail.table-delete-tooltip': 'Smazat položku',
        'preliminary.detail.corrective.panel-title': 'Nápravná opatření',
        'preliminary.detail.corrective.table-description': 'Opatření',
        'preliminary.detail.corrective.description-placeholder': 'Popis nápravného opatření',
        'preliminary.detail.corrective.description-tooltip': 'Popis nápravného opatření - pole je povinné',
        'preliminary.detail.corrective.add-tooltip': 'Přida popis nápravného opatření',
        'preliminary.detail.corrective.wizard.title': 'Průvodce nápravným opatřením',
        'preliminary.detail.corrective.wizard.step-title': 'Popis nápravného opatření',
        'preliminary.detail.eventtype.panel-title': 'Popis typu události',
        'preliminary.detail.eventtype.panel-title-tooltip': 'Alespoň jedna klasifikace události je vyžadována',
        'preliminary.detail.eventtype.table-type': 'Typ události',
        'preliminary.detail.eventtype.table-summary': 'Popis',
        'preliminary.detail.eventtype.add-tooltip': 'Přidat popis typu události',
        'preliminary.detail.occurrence-category.label': 'Klasifikace události',
        'preliminary.detail.arms.panel-title': 'ARMS atributy',
        'preliminary.detail.arms.barriers-label': 'Efektivita bariér',
        'preliminary.detail.arms.barriers-tooltip': 'Jaká byla efektivita bariér zbývajících mezi touto událostí a ' +
        'nejpravděpodobnějším nehodovým scénářem?',
        'preliminary.detail.arms.outcomes-label': 'Nejpravděpodobnější následek nehody',
        'preliminary.detail.arms.outcomes-tooltip': 'Pokud by tato událost vyústila v nehodu, jaký by byl její ' +
        'nejpravděpodobnější následek?',

        'wizard.finish': 'Dokončit',
        'wizard.next': 'Další',
        'wizard.previous': 'Předchozí',
        'wizard.advance-disabled-tooltip': 'Některá povinná pole nejsou vyplněna',

        'aircraft.registration': 'Registrace letadla',
        'aircraft.state-of-registry': 'Stát registrace',
        'flight.departure': 'Poslední místo odletu',
        'flight.destination': 'Plánovaný cíl',
        'flight.operation-type': 'Typ letu',
        'flight.operation-type.passenger': 'Civilní let',
        'flight.operation-type.passenger.tooltip': 'Let přepravující platící cestující. Zahrnuje též lety, které kromě pasažérů' +
        ' přepravují i náklad či poštu.',
        'flight.operation-type.cargo': 'Nákladní let',
        'flight.operation-type.cargo.tooltip': 'Pouze pro nákladní lety. Tyto zahrnují přepravu nákladu, samostatných zavazadel ' +
        'či pošty.',

        'eventtype.title': 'Typ události',
        'eventtype.default.description': 'Popis',
        'eventtype.default.description-placeholder': 'Popis události',
        'eventtype.incursion.lvp.label': 'Status nízké viditelnosti',
        'eventtype.incursion.location.label': 'Poloha',
        'eventtype.incursion.location.step-title': 'Místo narušení',
        'eventtype.incursion.intruder.step-title': 'Objekt vniknuvší na ranvej',
        'eventtype.incursion.intruder.aircraft': 'Letadlo',
        'eventtype.incursion.intruder.vehicle': 'Vozidlo',
        'eventtype.incursion.intruder.person': 'Osoba',
        'eventtype.incursion.intruder.organization': 'Organizace/oddělení letiště',
        'eventtype.incursion.intruder.person.panel-title': 'Narušitelem je osoba',
        'eventtype.incursion.intruder.person.category': 'Kategorie osoby',
        'eventtype.incursion.intruder.person.category-tooltip': 'Kategorie pozemního personálu',
        'eventtype.incursion.intruder.person.organization-tooltip': 'Organizace/oddělení letiště, ke kterému osoba patří',
        'eventtype.incursion.intruder.person.wasdoing': 'Co dělala osoba na ranveji?',
        'eventtype.incursion.intruder.vehicle.panel-title': 'Narušitelem je vozidlo',
        'eventtype.incursion.intruder.vehicle.type': 'Typ vozidla',
        'eventtype.incursion.intruder.vehicle.type-tooltip': 'Typ letištního vozidla, které vniklo na ranvej',
        'eventtype.incursion.intruder.vehicle.callsign': 'Call sign vozidla',
        'eventtype.incursion.intruder.vehicle.callsign-tooltip': 'Call sign vozidla',
        'eventtype.incursion.intruder.vehicle.organization-tooltip': 'Organizace/oddělení letiště, které vozidlo používá',
        'eventtype.incursion.intruder.vehicle.isats': 'Je vozidlo kontrolováno jednotkou ATS?',
        'eventtype.incursion.intruder.vehicle.hasradio': 'Má vozidlo nainstalovanou/funkční vysílačku?',
        'eventtype.incursion.intruder.vehicle.wasdoing': 'Co dělalo vozidlo na ranveji?',
        'eventtype.incursion.intruder.aircraft.panel-title': 'Narušitelem je letadlo',
        'eventtype.incursion.intruder.aircraft.callsign': 'Call sign',
        'eventtype.incursion.intruder.aircraft.operator': 'Operátor',
        'eventtype.incursion.intruder.aircraft.flight': 'Let',
        'eventtype.incursion.intruder.aircraft.nonflight': 'Pohyb po zemi',
        'eventtype.incursion.intruder.aircraft.operation': 'Let',
        'eventtype.incursion.intruder.aircraft.flightno': 'Číslo letu',
        'eventtype.incursion.intruder.aircraft.phase': 'Fáze',
        'eventtype.incursion.intruder.aircraft.phase-tooltip': 'Co letadlo dělalo?',
        'eventtype.incursion.intruder.aircraft.phase.taxi-to-rwy': 'Pojíždění k ranveji',
        'eventtype.incursion.intruder.aircraft.phase.taxi-to-rwy-tooltip': 'Začíná když se letadlo začne vlastní silou ' +
        'přesouvat od brány/rampy a končí při dosažení ranveje',
        'eventtype.incursion.intruder.aircraft.phase.taxi-to-takeoff': 'Pojíždění do vzletové pozice',
        'eventtype.incursion.intruder.aircraft.phase.taxi-to-takeoff-tooltip': 'Od vjezdu na ranvej do dosažení vzletové pozice',
        'eventtype.incursion.intruder.aircraft.phase.taxi-from-rwy': 'Pojíždění z ranveje',
        'eventtype.incursion.intruder.aircraft.phase.taxi-from-rwy-tooltip': 'Začíná při opuštění přistávací ranveje a ' +
        'končí při příjezdu k bráně, rampě, parkovací ploše, kdy se letadlo přestane pohybovat vlastní silou',
        'eventtype.incursion.intruder.aircraft.phase.maintain-position': 'Udržování pozice',
        'eventtype.incursion.intruder.aircraft.phase.maintain-position-tooltip': 'Udržování pozice na záchytném bodě',
        'eventtype.incursion.intruder.aircraft.phase.nonflight': 'Jiné pojíždění',
        'eventtype.incursion.intruder.aircraft.nonflight.operation': 'Jiná letecká událost',
        'eventtype.incursion.wasconflicting.step-title': 'Přítomnost jiného letadla',
        'eventtype.incursion.wasconflicting.panel-title': 'Přítomnost jiného letadla',
        'eventtype.incursion.wasconflicting.text': 'Bylo přítomno i jiné letadlo?',
        'eventtype.incursion.conflicting.step-title': 'Letadlo s povolením',
        'eventtype.incursion.conflicting.phase.takeoff': 'Vzlet',
        'eventtype.incursion.conflicting.phase.takeoff-tooltip': 'Letadlo vzlétalo',
        'eventtype.incursion.conflicting.phase.approach': 'Přiblížení',
        'eventtype.incursion.conflicting.phase.approach-tooltip': 'Letadlo se blížilo k ranveji',
        'eventtype.incursion.conflicting.phase.landing': 'Přistání',
        'eventtype.incursion.conflicting.phase.landing-tooltip': 'Letadlo přistávalo',

        'investigation.type': 'Šetření',
        'investigation.detail.panel-title': 'Hlášení o šetření',

        'factors.panel-title': 'Faktory',
        'factors.scale': 'Měřítko',
        'factors.scale-tooltip': 'Kliknutím vyberete měřítko: ',
        'factors.scale.second': 'Sekundy',
        'factors.scale.minute': 'Minuty',
        'factors.scale.hour': 'Hodiny',
        'factors.scale.relative': 'Relativní',
        'factors.scale.relative-tooltip': 'Kliknutím vyberete relativní měřítko',
        'factors.causes': 'Způsobuje',
        'factors.mitigates': 'Zmírňuje',
        'factors.link-type-select': 'Typ vztahu mezi faktory?',
        'factors.link-type-select-tooltip': 'Vyberte typ vztahu',
        'factors.link.delete.title': 'Smazat link?',
        'factors.link.delete.text': 'Určitě chcete smazat spojení vedoucí z faktoru {source} do faktoru {target}?',
        'factors.detail.title': 'Faktor události',
        'factors.detail.type': 'Typ faktoru',
        'factors.detail.type-placeholder': 'Typ faktoru',
        'factors.detail.time-period': 'Specifikace času',
        'factors.detail.start': 'Faktor nastal',
        'factors.detail.duration': 'Trvání',
        'factors.duration.second': '{duration, plural, =0 {sekund} one {sekunda} few {sekundy} other {sekund}}',
        'factors.duration.minute': '{duration, plural, =0 {minut} one {minuta} few {minuty} other {minut}}',
        'factors.duration.hour': '{duration, plural, =0 {hodin} one {hodina} few {hodiny} other {hodin}}',
        'factors.detail.details': 'Detail faktoru',
        'factors.detail.delete.title': 'Smazat faktor?',
        'factors.detail.delete.text': 'Určitě chcete smazat tento faktor?',

        'notfound.title': 'Nenalezeno',
        'notfound.msg-with-id': 'Záznam \'{resource}\' s identifikátorem {identifier} nenalezen.',
        'notfound.msg': 'Záznam \'{resource}\' nenalezen.',

        'notrenderable.title': 'Nelze zobrazit záznam',
        'notrenderable.error': 'Chyba: {message}',
        'notrenderable.error-generic': 'Zkontrolujte, prosím, zda je záznam validní.',

        'revisions.label': 'Revize zprávy',
        'revisions.created': 'Vytvořeno',
        'revisions.show-tooltip': 'Zobrazit tuto revizi',
        'revisions.readonly-notice': 'Starší revize jsou pouze ke čtení.'
    }
};