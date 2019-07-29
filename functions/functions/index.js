const functions = require('firebase-functions');
const firebaseAdmin = require('firebase-admin');
const {
    Storage
} = require('@google-cloud/storage');

// Create and Deploy Your First Cloud Functions
// https://firebase.google.com/docs/functions/write-firebase-functions

exports.helloWorld = functions.https.onCall((data, context) => {

    return new Promise((resolve, reject) => {
        const storage = new Storage();
        const bucket = storage.bucket('muzei-mtg.appspot.com');

        bucket.getFiles((err, files) => {
            if (!err) {
                // files is an array of File objects.
                var fileURLs = []; // array to hold all file urls 

                files.forEach((file) => {
                    fileURLs.push(file.metadata);
                });

                resolve(fileURLs)
            }
        });
    });
});