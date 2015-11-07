var RewirePlugin = require('rewire-webpack');

module.exports = {
    entry: {
        app: ['./js', 'webpack/hot/dev-server'],
        tests: ['./tests/__tests__', 'webpack/hot/dev-server']
    },
    output: {
        path: __dirname + '/dist',
        filename: '[name].bundle.js'
    },
    module: {
        loaders: [{
            test: /\.js$/,
            exclude: /node_modules/,
            loader: 'babel-loader'
        },{
            test: /node_modules\/(jsdom|node-fetch)/,
            loader: 'null-loader'
        }]
    },
    plugins: [
        new RewirePlugin()
    ]
};