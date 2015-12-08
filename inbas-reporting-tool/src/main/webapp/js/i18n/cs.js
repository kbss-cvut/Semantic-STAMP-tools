/**
 * Czech localization.
 */

var Constants = require('../constants/Constants');

module.exports = {
    'locales': ['cs'],

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
        'login': {
            'title': Constants.APP_NAME + ' - Přihlášení',
            'username': 'Uživatelské jméno',
            'password': 'Heslo',
            'submit': 'Přihlásit',
            'register': 'Registrace',
            'error': 'Přihlášení se nezdařilo.'
        },
        'register': {
            'title': Constants.APP_NAME + ' - Nový uživatel',
            'first-name': 'Jméno',
            'last-name': 'Příjmení',
            'username': 'Uživatelské jméno',
            'password': 'Heslo',
            'password-confirm': 'Potvrzení hesla',
            'passwords-not-matching-tooltip': 'Heslo a jeho potvrzení se neshodují',
            'submit': 'Registrovat',
            'mask': 'Registruji...'
        },
        'main': {
            'dashboard-nav': 'Hlavní strana',
            'preliminary-nav': 'Předběžná hlášení',
            'investigations-nav': 'Šetření',
            'logout': 'Odhlásit se'
        },
        'dashboard': {
            'welcome': 'Dobrý den, {name}, vítejte v ' + Constants.APP_NAME + '.',
            'create-tile': 'Vytvořit hlášení o události',
            'search-tile': 'Hledat hlášení',
            'search-placeholder': 'Název hlášení',
            'view-all-tile': 'Prohlížet všechna hlášení',
            'create-empty-tile': 'Začít s prázdným hlášením',
            'create-import-tile': 'Importovat počáteční hlášení',
            'recent-panel-heading': 'Nedávno přidaná/upravená hlášení',
            'recent-table-headline': 'Název hlášení',
            'recent-table-date': 'Datum události',
            'recent-table-last-edited': 'Naposledy upraveno'
        },
        'reports': {
            'no-occurrence-reports': 'Zatím nebylo vytvořeno žádné hlášení.',
            'no-reports': 'Zatím nebylo vytvořeno žádné hlášení.',
            'open-tooltip': 'Kliknutím zobrazíte detail hlášení',
            'edit-tooltip': 'Editovat toto hlášení',
            'delete-tooltip': 'Smazat toto hlášení',
            'loading-mask': 'Nahrávám hlášení...',
            'panel-title': 'Hlášení událostí',
            'table-date': 'Datum a čas události',
            'table-type': 'Typ'
        },
        'delete-dialog': {
            'title': 'Smazat hlášení?',
            'content': 'Skutečně chcete smazat toto hlášení?',
            'submit': 'Smazat'
        },
        'occurrence': {
            'headline-tooltip': 'Krátké pojmenování události - pole je povinné',
            'start-time': 'Počátek události',
            'start-time-tooltip': 'Datum a čas kdy k události došlo',
            'end-time': 'Konec události',
            'end-time-tooltip': 'Datum a čas kdy událost skončila',
            'class': 'Třída závažnosti',
            'class-tooltip': 'Třída závažnosti - pole je povinné'
        },
        'initial': {
            'panel-title': 'Prvotní hlášení',
            'table-report': 'Hlášení',
            'wizard-add-title': 'Přidat prvotní hlášení',
            'wizard-edit-title': 'Editovat prvotní hlášení'
        },
        'preliminary': {
            'panel-title': 'Předběžná hlášení',
            'table-investigate': 'Šetřit',
            'table-investigate-tooltip': 'Zahájit šetření události',
            'detail': {
                'loading-mask': 'Načítám hlášení...',
                'panel-title': 'Předběžné hlášení o události',
                'save-tooltip': 'Uložit změny',
                'saving': 'Ukládám...',
                'invalid-tooltip': 'Některá povinná pole nejsou vyplněna',
                'last-edited-msg': 'Naposledy upravil(a) {name} {date}.',
                'narrative-tooltip': 'Popis - pole je povinné',
                'table-edit-tooltip': 'Editovat položku',
                'table-delete-tooltip': 'Smazat položku',
                'corrective': {
                    'panel-title': 'Nápravná opatření',
                    'table-description': 'Opatření',
                    'description-placeholder': 'Popis nápravného opatření',
                    'add-tooltip': 'Přida popis nápravného opatření',
                    'wizard-title': 'Průvodce nápravným opatřením',
                    'wizard-step-title': 'Popis nápravného opatření'
                }
            }
        },
        'wizard': {
            'finish': 'Dokončit',
            'next': 'Další',
            'previous': 'Předchozí',
            'advance-disabled-tooltip': 'Některá povinná pole nejsou vyplněna'
        }
    }
};