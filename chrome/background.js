let url = "www.change.me";

chrome.runtime.onInstalled.addListener(() => {
    chrome.storage.sync.set( { url } );
});