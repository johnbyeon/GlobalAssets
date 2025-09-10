document.addEventListener('DOMContentLoaded', function () {
    function convertToCandleData(data) {
        return data.map(d => ({
            x: new Date(d.time),
            o: d.open,
            h: d.high,
            l: d.low,
            c: d.close
        }));
    }

    const stockCtx = document.getElementById('stockChart').getContext('2d');
    new Chart(stockCtx, {
        type: 'candlestick',
        data: { datasets: [{ label: '주식 1분봉', data: convertToCandleData(stockData), color: { up: 'red', down: 'blue', unchanged: 'gray' } }] },
        options: { responsive: true, maintainAspectRatio: false }
    });

    const coinCtx = document.getElementById('coinChart').getContext('2d');
    new Chart(coinCtx, {
        type: 'candlestick',
        data: { datasets: [{ label: '코인 1분봉', data: convertToCandleData(coinData), color: { up: 'red', down: 'blue', unchanged: 'gray' } }] },
        options: { responsive: true, maintainAspectRatio: false }
    });
});
