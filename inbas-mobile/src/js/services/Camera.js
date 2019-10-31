/*
 * Copyright (c) 2013, Czech Technical University in Prague, All rights reserved.
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


// saveToPhotoAlbum=true  -> content://media/external/images/media/13607
// saveToPhotoAlbum=false -> file:///mnt/sdcard/Android/data/cz.cvut.kbss.mondis_mobile/cache/1367236336829.jpg
// recording video, saveToPhotoAlbum=false -> {"name":"20140305_194700.mp4","fullPath":"file:/storage/emulated/0/DCIM/Camera/20140305_194700.mp4","type":"video/mp4","lastModifiedDate":1394045225000,"size":3616849,"start":0,"end":0}

angular.module('InbasApp.camera', ['InbasApp.storage'])
.factory('camera', [function () {
    cameraControl = new CameraControl();
    return cameraControl;
}])




function CameraControl() {
    this._pictureSource = navigator.camera.PictureSourceType;
    this._destinationType = navigator.camera.DestinationType;
    this._encodingType = navigator.camera.EncodingType;
    this._mediaType = navigator.camera.MediaType; //Camera.MediaType
    this._callback = null;
}


CameraControl.prototype.capturePhoto = function(callback) {
    console.log('Capturing photo');
    this._callback = callback;
    navigator.camera.getPicture(this._onPhotoDataSuccess.bind(this), this._fail.bind(this),
        {sourceType: this._pictureSource.CAMERA, encodingType:
            this._encodingType.JPEG, targetWidth: 640, targetHeight: 480, quality: 80,
            //destinationType: this._destinationType.DATA_URL});
            destinationType: this._destinationType.FILE_URI, saveToPhotoAlbum: false, allowEdit: true});
};

CameraControl.prototype.captureVideo = function(callback) {
    console.log('Capturing video');
    this._callback = callback;
    navigator.device.capture.captureVideo(this._onVideoSuccess.bind(this), this._failVideo.bind(this), {limit: 1});
};

CameraControl.prototype._onVideoSuccess = function(mediaFiles) {
    if (mediaFiles.length == 0) {
        console.log('Video camera captured no video');
        if (this._callback) {
            this._callback.call(this, {'error': 'No video captured'});
            this._callback = null;
        }
        return;
    }
    var file = mediaFiles[0];
    console.log('Video camera succeeded, file = '+file.fullPath);
    //file.name;
    var filename = file.fullPath;
    if (storage._isFileUri(filename)) {
        storage.moveImageByFileUri(filename, this._moveImageCB.bind(this));
    } else {
        if (filename.indexOf('file:') == 0) { // invalid URI - neither filename, nor URI, strip erroneous "file:"
            filename = filename.substr(5);
        }
        storage.moveImageByFileName(filename, this._moveImageCB.bind(this));
    }
};

CameraControl.prototype._onPhotoDataSuccess = function(imageUri) {
    console.log('Camera succeeded, file = '+imageUri);

    //this._callback.call(this, {'file': imageUri, 'ok': true}); // for Ripple testing
    storage.moveImageByFileUri(imageUri, this._moveImageCB.bind(this));
};

CameraControl.prototype._moveImageCB = function(response) {
    if (response.ok) {
        if (this._callback) {
            //this._callback.call(this, {'data': imageData, 'ok': true});
            //this._callback.call(this, {'file': imageUri, 'ok': true});
            this._callback.call(this, {'file': response.file, 'ok': true});
            this._callback = null;
        }
    } else {
        if (this._callback) {
            this._callback.call(this, {'error': response.error});
            this._callback = null;
        }
    }
};

CameraControl.prototype._fail = function(message) {
    console.log('Camera failed: '+message);
    if (this._callback) {
        this._callback.call(this, {'error': message});
        this._callback = null;
    }
};

CameraControl.prototype._failVideo = function(error) {
    console.log('Video camera failed: '+error.code);
    if (this._callback) {
        this._callback.call(this, {'error': error.code});
        this._callback = null;
    }
};


/*function onPhotoURISuccess(imageURI) {
    // Uncomment to view the image file URI
    // console.log(imageURI);

    var largeImage = document.getElementById('largeImage');

    largeImage.style.display = 'block';

    largeImage.src = imageURI;
}*/


/*function getPhoto(source) {
    navigator.camera.getPicture(onPhotoURISuccess, onFail, { quality: 50,
        destinationType: this._destinationType.FILE_URI,
        sourceType: source });
}*/
