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

function fetchCombinedAverage() {
    const type1 = document.getElementById("type1").value;
    const type2 = document.getElementById("type2").value;

    fetch(`${BASE_URL}/combined-average-rating?type1=${type1}&type2=${type2}`)
        .then(response => response.json())
        .then(data => {
            const output = document.getElementById("output");
            output.innerHTML = `<h3>Combined Average Rating</h3>
                                <p><b>${type1}</b> + <b>${type2}</b> → <strong>${data.toFixed(2)}</strong></p>`;
        })
        .catch(error => console.error("Error fetching combined average rating:", error));
}