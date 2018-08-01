var refreshButton = document.getElementById('refreshButton');
var createFolderButton = document.getElementById('createFolderButton');
var createDocumentButton = document.getElementById('createDocumentButton');
var goToButton = document.getElementById('goToButton');
var upButton = document.getElementById('upButton');
var nameInput = document.getElementById('name');
var canvas = document.getElementById('canvas');

function request(url) {
    var xhr = new XMLHttpRequest();
    xhr.open('GET', url, false);
    xhr.send();
    if (xhr.status !== 200) {
        alert( xhr.status + ': ' + xhr.statusText );
    } else {
        return xhr.responseText;
    }
}

function addContainable(containable, parent) {
    var div = document.createElement('div');
    var span = document.createElement('span')
    var isCurrent = containable.current;
    var isFolder = containable.containables;
    var name = containable.name;
    if (isFolder) {
        span.classList.add("folder");
    } else {
        span.classList.remove("folder");
    }
    if (isCurrent) {
        span.classList.toggle("current");
    } else {
        span.classList.remove("current");
    }
    span.innerHTML = name;
    div.innerHTML = span.outerHTML;
    div.style.marginLeft = '10px';
    parent.appendChild(div);
    if (isFolder) {
        containable.containables.forEach(function(item, i, arr) {
            addContainable(item, div);
        });
    }
}

function showHierarchy(json) {
    canvas.innerHTML = '';
    console.log(json);
    var hierarchy = JSON.parse(json);
    addContainable(hierarchy.folder, canvas)
}

function checkName(fnIfOk) {
    if (nameInput.value.trim() === '') {
        alert('Enter name!');
    } else {
        fnIfOk();
    }
}

refreshButton.onclick = function () {
    showHierarchy(request('hierarchy?action=show'))
};

createFolderButton.onclick = function() {
    checkName(function() {showHierarchy(request('hierarchy?action=createFolder&name=' + nameInput.value))});
};

createDocumentButton.onclick = function() {
    checkName(function() {showHierarchy(request('hierarchy?action=createDocument&name=' + nameInput.value))});
};

goToButton.onclick = function() {
    checkName(function() {showHierarchy(request('hierarchy?action=goTo&name=' + nameInput.value))});
};

upButton.onclick = function () {
    showHierarchy(request('hierarchy?action=up'))
};