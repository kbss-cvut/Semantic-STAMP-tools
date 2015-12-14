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
        'cancel-tooltip': 'Zrušit a zahodit změny',
        'save': 'Uložit',
        'delete': 'Smazat',
        'headline': 'Název',
        'narrative': 'Popis',
        'table-actions': 'Akce',
        'table-edit': 'Editovat',
        'save-success-message': 'Hlášení úspěšně uloženo.',
        'save-failed-message': 'Hlášení nelze uložit. Odpověď serveru: ',
        'author': 'Autor',
        'author-title': 'Autor hlášení',
        'description': 'Popis',

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
        'main.preliminary-nav': 'Předběžná hlášení',
        'main.investigations-nav': 'Šetření',
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
        'reports.no-reports': 'Zatím nebylo vytvořeno žádné hlášení.',
        'reports.open-tooltip': 'Kliknutím zobrazíte detail hlášení',
        'reports.edit-tooltip': 'Editovat toto hlášení',
        'reports.delete-tooltip': 'Smazat toto hlášení',
        'reports.loading-mask': 'Nahrávám hlášení...',
        'reports.panel-title': 'Hlášení událostí',
        'reports.table-date': 'Datum a čas události',
        'reports.table-type': 'Typ',

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
        'initial.wizard-add-title': 'Přidat prvotní hlášení',
        'initial.wizard-edit-title': 'Editovat prvotní hlášení',
        'initial.label': 'Prvotní hlášení',

        'preliminary.panel-title': 'Předběžná hlášení',
        'preliminary.table-investigate': 'Šetřit',
        'preliminary.table-investigate-tooltip': 'Zahájit šetření události',
        'preliminary.detail.loading-mask': 'Načítám hlášení...',
        'preliminary.detail.panel-title': 'Předběžné hlášení o události',
        'preliminary.detail.save-tooltip': 'Uložit změny',
        'preliminary.detail.saving': 'Ukládám...',
        'preliminary.detail.invalid-tooltip': 'Některá povinná pole nejsou vyplněna',
        'preliminary.detail.last-edited-msg': 'Naposledy upravil(a) {name} {date}.',
        'preliminary.detail.narrative-tooltip': 'Popis - pole je povinné',
        'preliminary.detail.table-edit-tooltip': 'Editovat položku',
        'preliminary.detail.table-delete-tooltip': 'Smazat položku',
        'preliminary.detail.corrective.panel-title': 'Nápravná opatření',
        'preliminary.detail.corrective.table-description': 'Opatření',
        'preliminary.detail.corrective.description-placeholder': 'Popis nápravného opatření',
        'preliminary.detail.corrective.add-tooltip': 'Přida popis nápravného opatření',
        'preliminary.detail.corrective.wizard-title': 'Průvodce nápravným opatřením',
        'preliminary.detail.corrective.wizard-step-title': 'Popis nápravného opatření',

        'wizard.finish': 'Dokončit',
        'wizard.next': 'Další',
        'wizard.previous': 'Předchozí',
        'wizard.advance-disabled-tooltip': 'Některá povinná pole nejsou vyplněna'
    }
};