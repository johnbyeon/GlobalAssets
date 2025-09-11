document.addEventListener('DOMContentLoaded', () => {
    initChart('stockChart', stockData, 1); // 주식 차트 (assetId=1)
    initChart('coinChart', coinData, 2);   // 코인 차트 (assetId=2)
});

function initChart(canvasId, initialData, assetId) {
    const ctx = document.getElementById(canvasId).getContext('2d');
    const chart = new Chart(ctx, {
        type: 'candlestick',
        data: {
            datasets: [{
                label: 'Price',
                data: initialData.map(item => ({
                    x: new Date(item.timestamp),
                    o: item.open,
                    h: item.high,
                    l: item.low,
                    c: item.close
                }))
            }]
        },
        options: {
            responsive: true,
            plugins: { legend: { display: false } },
            scales: {
                x: { type: 'time', time: { unit: 'minute', tooltipFormat: 'yyyy-MM-dd HH:mm' } },
                y: { title: { display: true, text: 'Price' } }
            }
        }
    });

    async function updateChart() {
        try {
            const res = await fetch(`/api/prices/${assetId}`);
            if (!res.ok) throw new Error('데이터 로드 실패');
            const data = await res.json();
            chart.data.datasets[0].data = data.map(item => ({
                x: new Date(item.timestamp),