const BASE_URL = "https://location-analyzer.onrender.com/locations";

function fetchData(endpoint) {
    fetch(`${BASE_URL}/${endpoint}`)
        .then(response => response.json())
        .then(data => displayData(endpoint, data))
        .catch(error => console.error('Error fetching data:', error));
}

function displayData(endpoint, data) {
    const output = document.getElementById("output");
    output.innerHTML = `<h3>${formatTitle(endpoint)}</h3>`;

    if (Array.isArray(data)) {
        if(data.length === 0){
            output.innerHTML += `<p>No incomplete data found.</p>`;
        }
        else{
            output.innerHTML += `<pre>${JSON.stringify(data, null, 2)}</pre>`;
        }
    } else {
        Object.keys(data).forEach(key => {
            output.innerHTML += `<p><b>${key}:</b> ${data[key]}</p>`;
        });
    }
}

function formatTitle(endpoint) {
    return endpoint.replace(/-/g, ' ').toUpperCase();
}
