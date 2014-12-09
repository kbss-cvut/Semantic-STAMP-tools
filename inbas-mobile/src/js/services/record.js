/*
 * Copyright (c) 2014, Czech Technical University in Prague,
 * All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */
"use strict";

angular.module('mondis.record', [
])

.service('theRecord', [function() {
	console.log('Record (service)');
	this.name = 'Starý hrad';
	this.detailNode = null;
	this.scrollPosition = 0;
	this.assesment = {
		name: 'root',
		children: [
			{
				name: 'one',
				collapsed: true,
				children: [
					{
						name: 'one.one',
						collapsed: true
					}, {
						name: 'one.two',
						collapsed: true
					}
				]
			}, {
				name: 'two',
				collapsed: true
			}, {
				name: 'three',
				collapsed: true
			}
		]
	};

	var nodeCount = 0;

	for (var i = 4; i <= 32; i++) {
		var child = { name: '#'+i, collapsed: true, children: [] };
		for (var j = 1; j <= 32; j++) {
			var child2 = { name: '#'+i+'.'+j, collapsed: true, children: [] };
			for (var k = 1; k <= 32; k++) {
				child2.children.push({ name: '#'+i+'.'+j+'.'+k, collapsed: true });
				nodeCount++;
			}
			child.children.push(child2);
			nodeCount++;
		}
		this.assesment.children.push(child);
		nodeCount++;
	}
	console.log('Generated', nodeCount, 'nodes.');

	var walkTree = function(nodes, parent_node) {
		for (var i in nodes) {
			var node = nodes[i];
			node.parent_node = parent_node;
			node.parent_i = i;
			node.completed = false;
			if ('children' in node) {
				walkTree(node.children, node);
			} else {
				node.children = [];
			}
		}
	};

	walkTree(this.assesment.children, this.assesment);
}]);


