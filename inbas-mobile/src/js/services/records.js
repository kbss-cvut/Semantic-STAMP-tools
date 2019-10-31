/*
 * Copyright (c) 2013-2014, Czech Technical University in Prague,
 * All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */
"use strict";

angular.module('InbasApp.records', [
		'InbasApp.storage'
	])
	.factory('records', ['storage', function(storage) {
		return new Records(storage);
	}]);


function Records(storage) {
	this.storage = storage;
	this.init();
}

Records.prototype.objects;

Records.prototype.NEW_RECORD = "nový";
Records.prototype.DIRTY_RECORD = "modifikovaný";
Records.prototype.CLEAN_RECORD = "aktuální";

Records.prototype._loadObjects = function(response, status) {
    var objects = [];
    if (this.isResponseValid(response)) {
        var loaded = JSON.parse(response.data);
        for (var object in loaded) {
            loaded[object].status = status;
            objects.push(new Record(loaded[object]));
        }
    }
    return objects;
}

Records.prototype.init = function(callback) {
    this.objects = [];
    this.storage.getAllRecords(function(data) {
        for (var index in data.list) {
            var i = 0; //je to jednovlaknove, takze tahle prasarna by mela fungovat
            this.storage.readRecord(data.list[index], function(recordJson) {
                this.objects.push(new Record(recordJson.data));
                i++;
                if(i == data.list.length){
                    callback();
                }
            }.bind(this));
        }
        if(data.list.length == 0){
            callback();
        }
    }.bind(this));
}

Records.prototype.getObject = function(id) {
    for (var record in this.objects) {
        if (id == this.objects[record].id) {
            return this.objects[record];
        }
    }
    return null;
}

Records.prototype.isResponseValid = function(response) {
    if (response.error || response.data == null || response.data.trim() == "") {
        return false;
    }
    return true;

}

Records.prototype.deleteRecord = function (recordId, callback) {
    // TODO new status for deleted records that were downloaded from the server.
    this.storage.deleteRecord(recordId, function(response) {
        if (response.error) {
            alert(languageMap.deleteRecordFailed);
        } else {
            for (var i in this.objects) {
                if(this.objects[i].id === recordId) {
                    this.objects.splice(i,1);
                    break;
                }
            }
        }
        callback();
    });
}

Records.prototype.updateRecord = function (record, callback) {

    this.storage.writeRecord(record.id, record, function(response) {
        if (response.error) {
            alert("Při ukládání došlo k chybě");
        }
        callback();
    });
}

Records.prototype._getRecordsForStatus = function(status, justIds) {
    var records = [];
    for (var o in this.objects) {
        if (this.objects[o].status == status) {
            var toPush = justIds ? this.objects[o].id : this.objects[o];
            records.push(toPush);
        }
    }
    return records;
}

Records.prototype.setDirty = function(r) {
    if (r.status != this.NEW_RECORD) {
        r.status = this.DIRTY_RECORD;
    }
}


