type Props = {
	children: any;
};

const Card = function ({ children }: Props) {
	return <section className="grid-card">{children}</section>;
};

export default Card;
