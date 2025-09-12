document.addEventListener('DOMContentLoaded', function () {
    let stockChart = null;
    let coinChart = null;

    function initializeChart(canvasId, data, label) {
        const canvas = document.getElementById(canvasId);
        if (!canvas) {
            console.error(`Canvas element ${canvasId} not found`);
            return null;
        }
        const ctx = canvas.getContext('2d');
        if (!ctx) {
            console.error(`Context unavailable for ${canvasId}`);
            return null;
        }
        if (!Array.isArray(data) || data.length === 0) {
            console.warn(`Invalid or empty data for ${canvasId}:`, data);
            return null;
        }
        console.log(`Initializing chart for ${canvasId} with ${data.length} items:`, data);
        try {
            const chart = new Chart(ctx, {
                type: 'candlestick',
                data: {
                    datasets: [{
                        label: label,
                        data: data.map(item => {
                            console.log('Mapping item:', item);
                            return {
                                t: new Date(item.timestamp),
                                o: item.open,
                                h: item.high,
                                l: item.low,
                                c: item.close
                            };
                        }),
                        borderColor: '#000000',
                        backgroundColor: data[0].open <= data[0].close ? '#00ff00' : '#ff0000',
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    scales: {
                        x: {
                            type: 'time',
                            time: { unit: 'minute', displayFormats: { minute: 'HH:mm' } },
                            title: { display: true, text: '시간' }
                        },
                        y: {
                            title: { display: true, text: '가격 (KRW)' },
                            beginAtZero: false
                        }
                    },
                    plugins: {
                        legend: { display: true, position: 'top' },
                        tooltip: { enabled: true }
                    }
                }
            });
            console.log(`Chart successfully initialized for ${canvasId}`);
            return chart;
        } catch (e) {
            console.error(`Chart initialization failed for ${canvasId}:`, e);
            return null;
        }
    }

    function fetchChartData(assetName, canvasId, isStock) {
        console.log(`Fetching data for ${assetName} at ${new Date()}`);
        fetch(`/chart/${assetName}`)
            .then(response => {
                if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
                return response.json();
            })
            .then(data => {
                console.log('Received data:', data);
                if (Array.isArray(data) && data.length > 0) {
                    const label = isStock ? `주식: ${assetName}` : `코인: ${assetName}`;
                    if (isStock && stockChart) stockChart.destroy();
                    else if (!isStock && coinChart) coinChart.destroy();
                    if (isStock) stockChart = initializeChart(canvasId, data, label);
                    else coinChart = initializeChart(canvasId, data, label);
                } else {
                    console.warn(`No valid data for ${assetName}, received:`, data);
                }
            })
            .catch(error => console.error('Fetch error:', error));
    }

    const assetSelector = document.getElementById('assetSelector');
    if (assetSelector) {
        assetSelector.addEventListener('change', function () {
            const assetName = this.value;
            const activeTab = document.querySelector('.nav-link.active')?.getAttribute('data-tab');
            if (!activeTab) {
                console.error('No active tab found');
                return;
            }
            const canvasId = activeTab === 'stock' ? 'stockChart' : 'coinChart';
            const isStock = activeTab === 'stock';
            console.log(`Selected asset: ${assetName}, canvas: ${canvasId}, isStock: ${isStock}`);
            if (assetName) fetchChartData(assetName, canvasId, isStock);
        });
    } else {
        console.error('assetSelector not found');
    }

    document.querySelectorAll('.nav-link').forEach(tab => {
        tab.addEventListener('click', function () {
            const assetName = assetSelector?.value;
            const canvasId = this.getAttribute('data-tab') === 'stock' ? 'stockChart' : 'coinChart';
            const isStock = this.getAttribute('data-tab') === 'stock';
            console.log(`Tab changed to ${canvasId}, asset: ${assetName}`);
            if (assetName) fetchChartData(assetName, canvasId, isStock);
        });
    });
});