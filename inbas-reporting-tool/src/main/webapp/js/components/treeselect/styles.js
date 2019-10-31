'use strict';

export default {
    component: {
        width: '100%',
        display: 'inline-block',
        verticalAlign: 'top',
        padding: '0px',
        '@media (max-width: 640px)': {
            width: '100%',
            display: 'block'
        }
    },
    searchBox: {
        padding: '20px 20px 0 20px'
    },
    viewer: {
        base: {
            fontSize: '12px',
            whiteSpace: 'pre-wrap',
            backgroundColor: '#777777',
            border: 'solid 1px black',
            padding: '0px',
            color: '#777777',
            minHeight: '250px'
        }
    },
    // see https://github.com/storybookjs/react-treebeard/issues/170
    // copied from react-treebeard/src/themes/default.js
    treeStyle: {
        tree: {
            base: {
                listStyle: 'none',
                backgroundColor: '#FFFFFF',
                margin: 0,
                padding: 0,
                color: '#9DA5AB',
                fontFamily: 'lucida grande ,tahoma,verdana,arial,sans-serif',
                fontSize: '12px'
            },
            node: {
                base: {
                    position: 'relative'
                },
                link: {
                    cursor: 'pointer',
                    position: 'relative',
                    padding: '0px 5px',
                    display: 'block'
                },
                activeLink: {
                    background: '#E8E8E8'
                },
                toggle: {
                    base: {
                        position: 'relative',
                        display: 'inline-block',
                        verticalAlign: 'top',
                        marginLeft: '-5px',
                        height: '24px',
                        width: '24px'
                    },
                    wrapper: {
                        position: 'absolute',
                        top: '50%',
                        left: '50%',
                        margin: '-7px 0 0 -7px',
                        height: '14px'
                    },
                    height: 14,
                    width: 14,
                    arrow: {
                            fill: '#9DA5AB',
                            strokeWidth: 0
                        }
                },
                header: {
                    base: {
                        display: 'inline-block',
                        verticalAlign: 'top',
                        color: '#337ab7'
                    },
                    connector: {
                        width: '2px',
                        height: '12px',
                        borderLeft: 'solid 2px black',
                        borderBottom: 'solid 2px black',
                        position: 'absolute',
                        top: '0px',
                        left: '-21px'
                    },
                    title: {
                        lineHeight: '24px',
                        verticalAlign: 'middle'
                    }
                },
                subtree: {
                    listStyle: 'none',
                    paddingLeft: '19px'
                },
                loading: {
                    color: '#AAAAEE'
                }
            }
        }
    }
};


