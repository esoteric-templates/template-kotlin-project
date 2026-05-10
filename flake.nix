{
	description = "A template repository for Kotlin projects";

	inputs = {
		nixpkgs.url = "github:NixOS/nixpkgs/nixos-25.11";
		flake-utils.url = "github:numtide/flake-utils";
	};

	outputs = {
		nixpkgs,
		flake-utils,
		...
	}: flake-utils.lib.eachDefaultSystem (system:
			let
				name = "template";

				pkgs = import nixpkgs {
					inherit system;
				};

				package = pkgs.stdenv.mkDerivation (finalAttrs: {
					inherit name;

					src = ./.;

					nativeBuildInputs = with pkgs; [
						gradle_9
						makeWrapper
					];

					buildInputs = with pkgs; [
						jre
					];

					mitmCache = pkgs.gradle.fetchDeps {
						pkg = package;
						data = ./deps.json;
					};

					gradleFlags = [
						"-Dfile.encoding=utf-8"
					];

					# gradleBuildTask = "dist";

					doCheck = true;

					installPhase = ''
						mkdir -p $out/share/${name}/
						# mkdir -p $out/bin/

						cp build/libs/*.jar $out/share/${name}/
					'';
				});
			in {
				packages.default = package;
				packages.deps-update = package.mitmCache.updateScript;

				devShell = pkgs.mkShell {
					buildInputs = with pkgs; [
						git
						gradle_9
					];
				};
			});
}
