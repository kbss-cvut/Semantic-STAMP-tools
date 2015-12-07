/**
 * Czech localization.
 */

var Constants = require('../constants/Constants');

module.exports = {
    'locales': ['cs'],

    'messages': {
        'back': 'Zpět',
        'cancel': 'Zrušit',
        'table-actions': 'Akce',
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
            'table-headline': 'Název',
            'table-date': 'Datum a čas události',
            'table-narrative': 'Popis',
            'table-type': 'Typ'
        },
        'delete': {
            'title': 'Smazat hlášení?',
            'content': 'Určitě chcete smazat toto hlášení?',
            'submit': 'Smazat',
        }
    }
};