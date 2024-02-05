# Blueprints.idea README

This plugin integrates IntelliJ IDEA with [Sublayer's Blueprint
Server](https://github.com/sublayerapp/blueprints) to allow you to store chunks
of code as blueprints to use as a base to generate new code given a description
of what you want to do. For more information visit [Sublayer
Blueprints](https://github.com/sublayerapp/blueprints)

## Installation

Run `git clone git@github.com:sublayerapp/blueprints.idea.git`

From your Plugins menu, select the gear icon in the header and select "Install
Plugin from Disk..." and navigate to the `blueprints-1.0-SNAPSHOT.zip` file in
the cloned folder.

## Usage

Make sure a [Sublayer Blueprints](https://github.com/sublayerapp/blueprints)
server is running at http://localhost:3000

Highlight a chunk of code, right click and choose `Store Blueprint` from the
context menu to send the chunk of code to your locally running blueprints
server for indexing.

Type out a description of the code you need to write, highlight the text, right
click and choose `Generate Blueprint Variant` from the context menu to replace
the description with code generated based off your stored blueprints.

## Community

Like what you see, or looking for more people working on the future of
programming with LLMs? Come join us in the [Promptable Architecture
Discord](https://discord.gg/sjTJszPwXt)
