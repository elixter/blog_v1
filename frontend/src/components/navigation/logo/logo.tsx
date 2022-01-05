import { Link } from 'react-router-dom';

const Logo = function () {
	return (
		<Link className="logo" to="/">
			<h1>SB</h1>
			<span>Devlog</span>
		</Link>
	);
};

export default Logo;
