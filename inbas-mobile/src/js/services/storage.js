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

angular.module('InbasApp.storage', [])
	.factory('storage', [function () {
		return new Storage();
	}]);


function Storage() {
	this.init(function () {
		console.log('Init callback has been called.');
	});
}

Storage.prototype.init = function (callback) {
    this._initCallback = callback;
    window.requestFileSystem(LocalFileSystem.PERSISTENT, 0, this._onFileSystemSuccess.bind(this), this._fail.bind(this));
};


Storage.prototype._onFileSystemSuccess = function(fileSystem) {
    console.log("FileSystem: "+fileSystem.root.name);
    this._fileSystem = fileSystem;
    fileSystem.root.getDirectory('mondis', {create: true, exclusive: false}, this._mondisDirCB.bind(this), this._fail.bind(this));
};

Storage.prototype._mondisDirCB = function(dir) {
    console.log('Mondis dir:', dir.fullPath);
    this._mondisDir = dir;
    dir.getDirectory('criteriasets', {create: true, exclusive: false}, this._criteriaSetsDirCB.bind(this), this._fail.bind(this));
    dir.getDirectory('records', {create: true, exclusive: false}, this._recordsDirCB.bind(this), this._fail.bind(this));
    dir.getDirectory('images', {create: true, exclusive: false}, this._imagesDirCB.bind(this), this._fail.bind(this));
    dir.getDirectory('mapdata', {create: true, exclusive: false}, this._mapDataDirCB.bind(this), this._fail.bind(this));
    dir.getDirectory('trees', {create: true, exclusive: false}, this._treeDirCB.bind(this), this._fail.bind(this));
};

Storage.prototype._criteriaSetsDirCB = function(dir) {
    console.log('CriteriaSetsDir:', dir.fullPath);
    this._criteriaSetsDir = dir;
};

Storage.prototype._recordsDirCB = function(dir) {
    console.log('RecordsDir:', dir.fullPath);
    this._recordsDir = dir;
};

Storage.prototype._imagesDirCB = function(dir) {
    console.log('ImagesDir:', dir.fullPath);
    this._imagesDir = dir;
};

Storage.prototype._mapDataDirCB = function(dir) {
    console.log('MapDataDir:', dir.fullPath);
    this._mapDataDir = dir;
};

Storage.prototype._treeDirCB = function(dir) {
    console.log('TreeDir:', dir.fullPath);
    this._treeDir = dir;

    this._initCallback();
};


Storage.prototype._fail = function(error) {
    console.error("File error:", error.code);
    if (this._callback) {
        this._callback.call(this, {'error': 'File error: ' + error.code});
    }
};

/**
 * Writes a Criteria Set by its symbolic URI
 * @param csUri
 * @param data - as JSON
 * @param callback
 */
Storage.prototype.writeCriteriaSet = function(csUri, data, callback) {
    console.log('Writing CS:', csUri);
    data = this._cleanPrivate(data);
    var that = {_data: JSON.stringify(data), _callback: callback, _truncate: true};
    this._criteriaSetsDir.getFile(this._encode(csUri), {create: true, exclusive: false}, this._writeGotFile.bind(that), this._fail.bind(that));
};

/**
 * Writes a Record by its symbolic URI
 * @param rUri
 * @param data - as JSON
 * @param callback
 */
Storage.prototype.writeRecord = function(rUri, data, callback) {
    console.log('Writing R:', rUri);
    data = this._cleanPrivate(data);
    var that = {_data: JSON.stringify(data), _callback: callback, _truncate: true};
    /*console.log(">>>"+this._encode(rUri));
    console.log("###"+JSON.stringify(that));
    console.log("@@@"+this._recordsDir);*/
    if (this._recordsDir) {
    	this._recordsDir.getFile(this._encode(rUri), {create: true, exclusive: false}, this._writeGotFile.bind(that), this._fail.bind(that));
    } else {
	    console.error('Trying to access _recordsDir too early (writeRecord).');
    }
};

/**
 * Writes an image by its symbolic URI
 * @param imgUri
 * @param data - image binary
 * @param callback
 */
Storage.prototype.writeImage = function(imgUri, data, callback) {
    console.log('Writing img:', imgUri);
    var that = {_data: data, _callback: callback, _truncate: true};
    this._imagesDir.getFile(this._encode(imgUri), {create: true, exclusive: false}, this._writeGotFile.bind(that), this._fail.bind(that));
};

/**
 * Writes a map data file by its symbolic URL
 * @param mapUrl
 * @param data - map data binary
 * @param callback
 */
Storage.prototype.writeMapData = function(mapUrl, data, callback, truncate) {
    console.log('Writing map data:', mapUrl);
    if (typeof truncate === 'undefined') {truncate = true;}
    var that = {_data: data, _callback: callback, _truncate: truncate};
    this._mapDataDir.getFile(this._encode(mapUrl), {create: true, exclusive: false}, this._writeGotFile.bind(that), this._fail.bind(that));
};

Storage.prototype.writeTree = function(treeUrl, data, callback) {
    console.log('Writing tree:', treeUrl);
    var that = {_data: JSON.stringify(data), _callback: callback, _truncate: true};
    this._treeDir.getFile(this._encode(treeUrl), {create: true, exclusive: false}, this._writeGotFile.bind(that), this._fail.bind(that));
};


Storage.prototype.downloadMap = function(mapUrl, callback) {
    console.log('Downloading map:', mapUrl+' to '+this._mapDataDir.fullPath+'/'+this._encode(mapUrl));
    if (this._webkitMode) {
        var oReq = new XMLHttpRequest();
        oReq.open("GET", mapUrl);
        oReq.responseType = "arraybuffer";
        oReq.timeout = 60000;
        oReq.ontimeout = function () {
            console.log("Downloading map timed out");
            callback.call(this, {'error': 'Download error: timeout'});
        };
        oReq.onload = function (oEvent) {
            console.log('Map:', mapUrl, 'loaded, saving to local storage ...');
            this.writeMapData(mapUrl, oReq.response, function (response) {
                console.log('Map:', mapUrl, 'Saving finished with result', response.ok);
                if (response.ok) {
                    console.log("Downloading map finished:", response.file);
                    callback.call(this, {ok: true, file: response.file});
                } else {
                    console.log("Downloading map failed:", response.error.code);
                    callback.call(this, {'error': 'Download error: ' + response.error.code});
                }
            }.bind(this), true);
        }.bind(this);
        oReq.send(null);
    } else {
        var ft = new FileTransfer();
        ft.download(encodeURI(mapUrl), this._mapDataDir.fullPath+'/'+this._encode(mapUrl), function(entry) {
            console.log("Downloading map finished:", entry.fullPath);
            callback.call(this, {ok: true, file: entry.fullPath});
        }, function(error) {
            console.log("Downloading map failed:", error.code);
            callback.call(this, {'error': 'Download error: '+error.code});
        }, false, {});
    }
};

Storage.prototype.downloadImage = function (imgUrl, imgUri, callback) {
    console.log('Downloading image:', imgUrl, ' to ', this._imagesDir.fullPath + '/' + this._encode(imgUri));

    if (this._webkitMode) {
        var oReq = new XMLHttpRequest();
        oReq.open("GET", imgUrl);
        oReq.responseType = "arraybuffer";
        oReq.onload = function (oEvent) {
            console.log('Image: ', imgUri, ' Loaded, saving to local storage ...');
            this.writeImage(imgUri, oReq.response, function (response) {
                console.log('Image:', imgUri, 'Saving finished with result', response.ok);
                if (response.ok) {
                    console.log("Downloading image finished:", response.file);
                    callback.call(this, {ok: true, file: response.file});
                } else {
                    console.log("Downloading image failed:", response.error.code);
                    callback.call(this, {'error': 'Download error: ' + response.error.code});
                }
            }.bind(this));
        }.bind(this);
        oReq.send(null);
    } else {
        var ft = new FileTransfer();
        ft.download(encodeURI(imgUrl), this._imagesDir.fullPath + '/' + this._encode(imgUri), function (entry) {
            console.log("Downloading image finished:", entry.fullPath);
            callback.call(this, {ok: true, file: entry.fullPath});
        }, function (error) {
            console.log("Downloading image failed:", error.code);
            callback.call(this, {'error': 'Download error: ' + error.code});
        }, false, {});
    }
};

/**
 * Writes arbitrary string to the Mondis root
 * @param filName
 * @param data - arbitrary string
 * @param callback
 */
Storage.prototype.write = function(fileName, data, callback) {
    console.log('Writing:', fileName);
    var that = {_data: data, _callback: callback, _truncate: true};
    this._mondisDir.getFile(fileName, {create: true, exclusive: false}, this._writeGotFile.bind(that), this._fail.bind(that));
    /*var that = new StorageCall(callback);
    that._data = data;
    that._truncate = true;
    this._mondisDir.getFile(fileName, {create: true, exclusive: false}, this._writeGotFile.bind(that), this._fail.bind(that));*/
};


Storage.prototype._writeGotFile = function(fileEntry) {
//StorageCall.prototype._writeGotFile = function(fileEntry) {
        console.log('Write got file:', fileEntry.fullPath);
    this._fileEntry = fileEntry;
    fileEntry.createWriter(this._writeWriter.bind(this), this._fail.bind(this));
    //fileEntry.createWriter(this._storage._writeWriter.bind(this), this._storage._fail.bind(this));
};

Storage.prototype._writeWriter = function(writer) {
    console.log('Writer started:', writer.fileName);
    writer.onwrite = function(evt) {
        console.log("Truncate success");
        this._writeWriterWrite.bind(this)(writer);
    }.bind(this);
    writer.onerror = function(evt) {
        console.log('Failed writer truncate/write: ', evt);
        this._fail.bind(this)(evt.target.error);
    }.bind(this);

    if (this._truncate) {
        writer.truncate(0);
    } else {
        writer.seek(writer.length);
        this._writeWriterWrite.bind(this)(writer);
    }
};

Storage.prototype._writeWriterWrite = function(writer) {
    if (this._data.length == 0) {
        console.log('Nothing to write, quitting');
        this._callback.call(this, {ok: true, file: this._fileEntry.fullPath});
        return;
    }
    writer.onwrite = function(evt) {
        console.log("Write success");
        this._callback.call(this, {ok: true, file: this._fileEntry.fullPath});
    }.bind(this);
    if (this._webkitMode) {
        writer.write(new Blob([this._data], {type: 'text/plain'}));
    } else {
        writer.write(this._data);
    }
};

/**
 * Reads a Criteria Set as JSON by given symbolic URI
 * @param csUri
 * @param callback
 */
Storage.prototype.readCriteriaSet = function(csUri, callback) {
    console.log('Reading CS:', csUri);
    var that = {_callback: callback, _parseJson: true};
    this._criteriaSetsDir.getFile(this._encode(csUri), {create: false, exclusive: false}, this._readGotFile.bind(that), this._fail.bind(that));
};

/**
 * Reads a Record as JSON by given symbolic URI
 * @param rUri
 * @param callback
 */
Storage.prototype.readRecord = function(rUri, callback) {
    console.log('Reading R:', rUri);
    var that = {_callback: callback, _parseJson: true};
    if (this._recordsDir) {
	this._recordsDir.getFile(this._encode(rUri), {create: false, exclusive: false}, this._readGotFile.bind(that), this._fail.bind(that));
    } else {
	    console.error('Trying to access _recordsDir too early (readRecord).');
    }
};

/**
 * Reads an image as binary by given symbolic URI
 * @param imgUri
 * @param callback
 */
Storage.prototype.readImage = function(imgUri, callback) {
    console.log('Reading img:', imgUri);
    var that = {_callback: callback, _parseJson: false};
    this._imagesDir.getFile(this._encode(imgUri), {create: false, exclusive: false}, this._readGotBase64File.bind(that), this._fail.bind(that));
};

Storage.prototype.readMapData = function(mapUrl, callback) {
    console.log('Reading map data:', mapUrl);
    var that = {_callback: callback, _parseJson: false};
    this._mapDataDir.getFile(this._encode(mapUrl), {create: false, exclusive: false}, this._readGotFile.bind(that), this._fail.bind(that));
};

Storage.prototype.readTree = function(treeUrl, callback) {
    console.log('Reading tree:', treeUrl);
    var that = {_callback: callback, _parseJson: true};
    this._treeDir.getFile(this._encode(treeUrl), {create: false, exclusive: false}, this._readGotFile.bind(that), this._fail.bind(that));
};

/**
 * Reads arbitrary text file from Mondis root
 * @param fileName
 * @param callback
 */
Storage.prototype.read = function(fileName, callback) {
    console.log('Reading:', fileName);
    var that = {_callback: callback, _parseJson: false};
    this._mondisDir.getFile(fileName, {create: false, exclusive: false}, this._readGotFile.bind(that), this._fail.bind(that));
};

Storage.prototype._readGotFile = function(fileEntry) {
    console.log('Read got file:', fileEntry.fullPath);
    fileEntry.file(this._readAsText.bind(this), this._fail.bind(this));
};

Storage.prototype._readGotBase64File = function(fileEntry) {
    console.log('Read got file:', fileEntry.fullPath);
    fileEntry.file(this._readAsDataURL.bind(this), this._fail.bind(this));
};

Storage.prototype._readRead = function(file) {
    console.log('Read file size:', file.size);
    var reader = new FileReader();
    reader.onloadend = function(evt) {
        console.log("Read complete");
        var data;
	if (evt.target.result == '') {
            console.log("No data received");
            data = null;
	} else if (this._parseJson) {
            data = JSON.parse(evt.target.result)
        } else {
            data = evt.target.result;
        }
        this._callback.call(this, {'data': data, 'ok': true, 'file': file.fullPath});
    }.bind(this);
    return reader;
    //FileError.ENCODING_ERR
};

Storage.prototype._readAsDataURL = function(file) {
    this._readRead.call(this, file).readAsDataURL(file);
}

Storage.prototype._readAsText = function(file) {
    this._readRead.call(this, file).readAsText(file);
}

Storage.prototype.getImageFileUri = function(imgUri, callback) {
    console.log('Getting img file uri:', imgUri);
    var that = {_callback: callback};
    this._imagesDir.getFile(this._encode(imgUri), {create: false, exclusive: false}, this._getFileUriGotFile.bind(that), this._fail.bind(that));
};

Storage.prototype.getMapDataFileUri = function(mapUrl, callback) {
    console.log('Getting map data file uri:', mapUrl);
    var that = {_callback: callback};
    this._mapDataDir.getFile(this._encode(mapUrl), {create: false, exclusive: false}, this._getFileUriGotFile.bind(that), this._fail.bind(that));
};

Storage.prototype.getMapDataFileUriNoCB = function(mapUrl) {
    console.log('Getting map data file uri:', mapUrl);
    var res = this._mapDataDir.toURL()+'/'+this._encode(mapUrl);
    console.log('Got:', res);
    return res;
};

Storage.prototype._getFileUriGotFile = function(fileEntry) {
    console.log('Got file URI:', fileEntry.fullPath);
    this._callback.call(this, {'ok': true, 'file': fileEntry.fullPath});
};



Storage.prototype._encode = function(uri) {
    return encodeURIComponent(uri).replace(/%/g, '_');
};

Storage.prototype._decode = function(name) {
    return decodeURIComponent(name.replace(/_/g, '%'));
};

Storage.prototype._isFileUri = function(name) {
    return name.indexOf('file://') == 0;
};

Storage.prototype.getAllRecords = function(callback) {
    console.log('Reading all R');
    if (this._recordsDir) {
  	var that = {_callback: callback};
	var dirReader = this._recordsDir.createReader();
	dirReader.readEntries(this._gotFiles.bind(that), this._fail.bind(that));
    } else {
	    console.error('Trying to access _recordsDir too early (getAllRecords).');
    }
};

Storage.prototype.getAllCriteriaSets = function(callback) {
    console.log('Reading all CS');
    var that = {_callback: callback};
    var dirReader = this._criteriaSetsDir.createReader();
    dirReader.readEntries(this._gotFiles.bind(that), this._fail.bind(that));
};

Storage.prototype._gotFiles = function(entries) {
    var res = new Array();
    for (var i = 0; i < entries.length; i++) {
        if (entries[i].isFile) {
            res.push(this._decode(entries[i].name));
        }
    }
    this._callback.call(this, {ok: true, list: res});
};


Storage.prototype.moveImageByFileName = function(fileName, callback) {
    console.log('Moving image by filename:', fileName);
    var that = {_callback: callback};
    this._fileSystem.root.getFile(fileName, {create: false, exclusive: false}, this._moveImageGotFile.bind(that), this._fail.bind(that));
};

Storage.prototype.moveImageByFileUri = function(fileUri, callback) {
    console.log('Moving image by URI:', fileUri);
    var that = {_callback: callback};

    //LocalFileSystem.resolveLocalFileSystemURI
    window.resolveLocalFileSystemURI(fileUri, this._moveImageGotFile.bind(that), this._fail.bind(that));
};

Storage.prototype._moveImageGotFile = function(fileEntry) {
    console.log('Moving got file:', fileEntry.fullPath);
    fileEntry.moveTo(this._imagesDir, fileEntry.name, this._moveSuccess.bind(this), this._fail.bind(this));
};


Storage.prototype._moveSuccess = function(fileEntry) {
    console.log('Moving succeeded:', fileEntry.fullPath);
    this._callback.call(this, {ok: true, file: fileEntry.fullPath});
};


Storage.prototype.copyImageByFileUriAndRename = function(fileUri, newName, callback) {
    console.log('Copying image:', fileUri);
    var that = {_callback: callback, _newName: newName};

    window.resolveLocalFileSystemURI(fileUri, this._copyImageGotFile.bind(that), this._fail.bind(that));
};

Storage.prototype._copyImageGotFile = function(fileEntry) {
    console.log('Copying got file:', fileEntry.fullPath);
    var newName = (this._newName == null) ? fileEntry.name : this._newName;
    fileEntry.copyTo(this._imagesDir, newName, this._copySuccess.bind(this), this._fail.bind(this));
};

Storage.prototype._copySuccess = function(fileEntry) {
    console.log('Copying succeeded:', fileEntry.fullPath);
    this._callback.call(this, {ok: true, file: fileEntry.fullPath});
};

/*Storage.prototype.renameImage = function(origName, newName, callback) {
    console.log('Renaming image:', origName);
    this._renameCallback = callback;
    this._renameDir = this._imagesDir;
    this._renameName = this._encode(newName);
    this._imagesDir.getFile(this._encode(origName), {create: false, exclusive: false}, this._renameGotFile.bind(this), this._fail.bind(this));
};

Storage.prototype._renameGotFile = function(fileEntry) {
    console.log('Rename got file:', fileEntry.fullPath);
    fileEntry.moveTo(this._renameDir, this._renameName, this._renameSuccess.bind(this), this._fail.bind(this));
};

Storage.prototype._renameSuccess = function(fileEntry) {
    console.log('Rename succeeded:', fileEntry.fullPath);
    this._renameCallback.call(this, {ok: true, file: fileEntry.fullPath});
    this._renameCallback = null;
};*/

Storage.prototype.assignImageUri = function(fileUri, imageUri, callback) {
    console.log('Assigning image URI:', fileUri);
    var that = {_callback: callback, _dir: this._imagesDir, _name: this._encode(imageUri)};
    if (this._isFileUri(fileUri)) {
        window.resolveLocalFileSystemURI(fileUri, this._assignGotFile.bind(that), this._fail.bind(that));
    } else {
        console.log('Directly is a file, using getFile method');
        this._fileSystem.root.getFile(fileUri, {create: false, exclusive: false}, this._assignGotFile.bind(that), this._fail.bind(that));
    }
};

Storage.prototype._assignGotFile = function(fileEntry) {
    console.log('Assign got file:', fileEntry.fullPath);
    fileEntry.moveTo(this._dir, this._name, this._assignSuccess.bind(this), this._fail.bind(this));
};

Storage.prototype._assignSuccess = function(fileEntry) {
    console.log('Assign succeeded:', fileEntry.fullPath);
    this._callback.call(this, {ok: true, file: fileEntry.fullPath});
};


Storage.prototype.getImageSrc = function(imgUri, callback) {
    if (this._webkitMode) {
        this.readImage(imgUri, function(resp) {
            callback.call(this, {ok: resp.ok, error: resp.error, file: resp.file, src: resp.data});
        });
    } else {
        this.getImageFileUri(imgUri, function(resp) {
            var url = resp.file;
            callback.call(this, {ok: resp.ok, error: resp.error, file: resp.file, src: url});
        });
    }
};

Storage.prototype.deleteCriteriaSet = function(csUri, callback) {
    console.log('Deleting CS:', csUri);
    var that = {_callback: callback};
    this._criteriaSetsDir.getFile(this._encode(csUri), {create: false, exclusive: false}, this._deleteGotFile.bind(that), this._fail.bind(that));
};

Storage.prototype.deleteRecord = function(rUri, callback) {
    console.log('Deleting R:', rUri);
    var that = {_callback: callback};
    this._recordsDir.getFile(this._encode(rUri), {create: false, exclusive: false}, this._deleteGotFile.bind(that), this._fail.bind(that));
};

Storage.prototype.deleteImage = function(imgUri, callback) {
    console.log('Deleting img:', imgUri);
    var that = {_callback: callback};
    this._imagesDir.getFile(this._encode(imgUri), {create: false, exclusive: false}, this._deleteGotFile.bind(that), this._fail.bind(that));
};

Storage.prototype.deleteMapData = function(mapUrl, callback) {
    console.log('Deleting map data:', mapUrl);
    var that = {_callback: callback};
    this._mapDataDir.getFile(this._encode(mapUrl), {create: false, exclusive: false}, this._deleteGotFile.bind(that), this._fail.bind(that));
};

/**
 * Delete arbitrary file.
 * @param mapUrl
 * @param callback
 */
Storage.prototype.delete = function(fileName, callback) {
    console.log('Deleting file:', fileName);
    var that = {_callback: callback};
    this._mondisDir.getFile(fileName, {create: false, exclusive: false}, this._deleteGotFile.bind(that), this._fail.bind(that));
};

Storage.prototype._deleteGotFile = function(fileEntry) {
    console.log('Delete got file:', fileEntry.fullPath);
    fileEntry.remove(this._deleted.bind(this), this._fail.bind(this));
};

Storage.prototype._deleted = function() {
    console.log('Deleted');
    this._callback.call(this, {ok: true});
};


Storage.prototype.deleteAllMapData = function(callback) {
    console.log('Deleting all map data');
    var that = {_callback: callback};
    var dirReader = this._mapDataDir.createReader();
    dirReader.readEntries(this._gotFilesToDelete.bind(that), this._fail.bind(that));
};

Storage.prototype.deleteAllCriteriaSets = function(callback) {
    console.log('Deleting all CS');
    var that = {_callback: callback};
    var dirReader = this._criteriaSetsDir.createReader();
    dirReader.readEntries(this._gotFilesToDelete.bind(that), this._fail.bind(that));
};

Storage.prototype._gotFilesToDelete = function(entries) {
    var tcnt = 0;
    for (var i = 0; i < entries.length; i++) {if (entries[i].isFile) {tcnt++;}}

    if (tcnt == 0) {
        this._callback.call(this, {ok: true, okCnt: 0, errorCnt: 0});
        return;
    }

    var that = {_totalCnt: tcnt, _okCnt: 0, _errorCnt: 0, _origCallback: this._callback,
      _callback: function(resp) {
        if (resp.ok) {
            this._okCnt++;
        } else {
            this._errorCnt++;
        }
        if (this._okCnt + this._errorCnt == this._totalCnt) {
            this._origCallback.call(this, {ok: (this._okCnt==this._totalCnt), okCnt: this._okCnt, errorCnt: this._errorCnt});
        }
    }};

    for (var i = 0; i < entries.length; i++) {
        if (entries[i].isFile) {
            this._deleteGotFile.call(that, entries[i]);
        }
    }
};

Storage.prototype._cleanPrivate = function(data) {
    var res = {};
    for (var prop in data) {
        if (prop.substr(0, 1) != '_') {
            res[prop] = data[prop];
        }
    }
    return res;
};
