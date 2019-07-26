const functions = require('firebase-functions');
const firebaseAdmin = require('firebase-admin');
const {
    Storage
} = require('@google-cloud/storage');
// require('@google-cloud/debug-agent').start({
//     allowExpressions: true
// });

if (!firebaseAdmin.apps.length) {
    firebaseAdmin.initializeApp({
        "projectId": "muzei-mtg",
        "databaseURL": "https://muzei-mtg.firebaseio.com",
        "storageBucket": "muzei-mtg.appspot.com",
        "locationId": "us-central",
        "apiKey": "AIzaSyBGE5o0ORgSAFdmYYC3Xr3DWuearR46wcA",
        "authDomain": "muzei-mtg.firebaseapp.com",
        "messagingSenderId": "551823486447"
    });
}

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